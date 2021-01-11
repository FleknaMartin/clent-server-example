package net.example.server.integration;

import io.grpc.stub.StreamObserver;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import net.example.grpc.PaymentServiceGrpc;
import net.example.server.grpc.mapping.UtilMapper;
import net.example.server.jpa.entity.Card;
import net.example.server.jpa.entity.Payment;
import org.aspectj.lang.annotation.Before;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lognet.springboot.grpc.security.GrpcSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static net.example.server.util.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BulkPaymentIntegrationTest extends BaseIntegrationTest {

    final List<PaymentResponse> responses = new ArrayList<>();

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    void initOneCardWithoutCardHolder() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = platformTransactionManager.getTransaction(definition);
        Card predefinedCard = new Card();
        predefinedCard.setCardNumber(CARD_NUMBER);
        entityManager.persist(predefinedCard);
        platformTransactionManager.commit(status);
    }

    @AfterEach
    public void clearResponses() {
        responses.clear();
    }

    @Test
    @Issue("I-3")
    @Description("Bulk payment success")
    @Transactional
    public void bulkPayment_success() {
        initOneCardWithoutCardHolder();

        AtomicReference<StreamObserver<PaymentRequest>> requestObserverRef = new AtomicReference<>();
        PaymentServiceGrpc.PaymentServiceStub stub
                = PaymentServiceGrpc.newStub(channel);
        StreamObserver<PaymentRequest> observer = stub.streamPayments(new TestStreamObserver());
        requestObserverRef.set(observer);
        List<PaymentTo> toSuccessRequestPayments = new ArrayList<>();
        List<PaymentTo> toFailedRequestPayments = new ArrayList<>();
        createSuccessPayments(observer, toSuccessRequestPayments);
        createFailedPayment(observer, toFailedRequestPayments);

        Awaitility.await().atMost(Duration.ofSeconds(10)).until(() -> responses.size() == 5);
        observer.onCompleted();

        List<Payment> payments = paymentRepository.findAll();
        Assertions.assertEquals(4, payments.size());

        Assertions.assertEquals(4, responses.stream()
                .filter(r -> PaymentResponse.Result.SUCCESS == r.getResult())
                .count());
        Assertions.assertEquals(1, responses.stream()
                .filter(r -> PaymentResponse.Result.FAILURE == r.getResult())
                .count());

        List<PaymentTo> resultPayments = payments.stream()
                .map(p -> {
                    String firstName = p.getCard().getCardHolder().getFirstName();
                    String lastName = p.getCard().getCardHolder().getLastName();
                    String cardNumber = p.getCard().getCardNumber();
                    BigDecimal amount = p.getAmount();
                    return new PaymentTo(firstName, lastName, cardNumber, amount);
                })
                .collect(Collectors.toList());
        assertThat(resultPayments).hasSameElementsAs(toSuccessRequestPayments);
        toFailedRequestPayments.stream().forEach(p -> assertThat(resultPayments).doesNotContain(p));
    }

    @Step("Create failed payment")
    private void createFailedPayment(StreamObserver<PaymentRequest> observer, List<PaymentTo> requestPayments) {
        processRequest(observer, requestPayments, createPayment(ANOTHER_FIRST_NAME, LAST_NAME, GRPC_BIG_DECIMAL, CARD_NUMBER));
    }

    @Step("Create success payments")
    private void createSuccessPayments(StreamObserver<PaymentRequest> observer, List<PaymentTo> requestPayments) {
        processRequest(observer, requestPayments, createPayment(FIRST_NAME, LAST_NAME, GRPC_BIG_DECIMAL, CARD_NUMBER));
        Awaitility.await().atMost(Duration.ofSeconds(10)).until(() -> responses.size() == 1);
        processRequest(observer, requestPayments, createPayment(FIRST_NAME, LAST_NAME, ANOTHER_GRPC_BIG_DECIMAL, CARD_NUMBER));
        processRequest(observer, requestPayments, createPayment(FIRST_NAME, LAST_NAME, GRPC_BIG_DECIMAL, ANOTHER_CARD_NUMBER));
        processRequest(observer, requestPayments, createPayment(ANOTHER_FIRST_NAME, LAST_NAME, GRPC_BIG_DECIMAL, THIRD_CARD_NUMBER));
    }

    private void processRequest(StreamObserver<PaymentRequest> observer, List<PaymentTo> requestPayments, PaymentRequest payment) {
        observer.onNext(payment);
        requestPayments.add(convert(payment));
    }

    private PaymentTo convert(PaymentRequest request) {
        return new PaymentTo(request.getFirstName(), request.getLastName(), request.getCardNumber(),
                UtilMapper.grpcBigDecimal2BigDecimal(request.getAmount()));
    }

    private class TestStreamObserver implements StreamObserver<PaymentResponse> {
        @Override
        public void onNext(PaymentResponse response) {
            BulkPaymentIntegrationTest.this.responses.add(response);
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }

        @Override
        public void onCompleted() {
        }
    }

    private class PaymentTo {
        String firstName;
        String LastName;
        String cardNumber;
        BigDecimal amount;

        public PaymentTo(String firstName, String lastName, String cardNumber, BigDecimal amount) {
            this.firstName = firstName;
            LastName = lastName;
            this.cardNumber = cardNumber;
            this.amount = amount;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String lastName) {
            LastName = lastName;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PaymentTo payment = (PaymentTo) o;
            return Objects.equals(firstName, payment.firstName) &&
                    Objects.equals(LastName, payment.LastName) &&
                    Objects.equals(cardNumber, payment.cardNumber) &&
                    Objects.equals(amount, payment.amount);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firstName, LastName, cardNumber, amount);
        }
    }
}