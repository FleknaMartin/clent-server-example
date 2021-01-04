package net.example.server.integration;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import net.example.grpc.BigDecimal;
import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import net.example.grpc.PaymentServiceGrpc;
import net.example.server.core.validation.util.massagepattern.ValidationPatterns;
import net.example.server.jpa.entity.Card;
import net.example.server.jpa.entity.CardHolder;
import net.example.server.jpa.entity.Payment;
import net.example.server.jpa.repository.CardHolderRepository;
import net.example.server.jpa.repository.CardRepository;
import net.example.server.jpa.repository.PaymentRepository;
import net.example.server.util.BasicAuthenticationCallCredentials;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import static net.example.server.util.TestConstants.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SinglePaymentIntegrationTest extends BaseIntegrationTest {

    @Test
    @Issue("I-1")
    @Description("Single payment success")
    @Transactional
    public void singlePayment_success() {
        PaymentServiceGrpc.PaymentServiceBlockingStub stub
                = PaymentServiceGrpc.newBlockingStub(channel)
                .withCallCredentials(credentials);

        PaymentRequest request = createPayment(FIRST_NAME, LAST_NAME, GRPC_BIG_DECIMAL, CARD_NUMBER);

        PaymentResponse response = processExchange(stub, request);

        validate_success(response);
    }

    @Test
    @Issue("I-2")
    @Description("Single payment missing first name")
    @Transactional
    public void singlePayment_missingFirstNameInvalidCardNUmber_failure() {
        PaymentServiceGrpc.PaymentServiceBlockingStub stub
                = PaymentServiceGrpc.newBlockingStub(channel)
                .withCallCredentials(credentials);

        PaymentRequest request = createPayment(StringUtils.EMPTY, FIRST_NAME, GRPC_BIG_DECIMAL, INVALID_CARD_NUMBER);

        PaymentResponse response = processExchange(stub, request);

        validate_MissingFirstNameInvalidCardNumber_Failure(response);
    }

    @Step("Validate result")
    private void validate_success(PaymentResponse response) {
        Assertions.assertEquals(PaymentResponse.Result.SUCCESS, response.getResult());
        Assertions.assertTrue(StringUtils.isBlank(response.getMessage()));

        List<Payment> allPayments = paymentRepository.findAll();
        Assertions.assertEquals(1, allPayments.size());
        Payment payment = allPayments.get(0);
        Assertions.assertEquals(BIG_DECIMAL, payment.getAmount());
        Assertions.assertEquals(CARD_NUMBER, payment.getCard().getCardNumber());
        Assertions.assertEquals(FIRST_NAME, payment.getCard().getCardHolder().getFirstName());
        Assertions.assertEquals(LAST_NAME, payment.getCard().getCardHolder().getLastName());
    }

    @Step("Validate results")
    private void validate_MissingFirstNameInvalidCardNumber_Failure(PaymentResponse response) {
        Assertions.assertEquals(PaymentResponse.Result.FAILURE, response.getResult());
        Assertions.assertEquals(MessageFormat.format(ValidationPatterns.INVALID_CARD_NUMBER.getPattern(), INVALID_CARD_NUMBER) +
                       ";" + MessageFormat.format(ValidationPatterns.NOT_BLANK.getPattern(), "firstName"),
                response.getMessage());

        Assertions.assertEquals(0, paymentRepository.findAll().size());
        Assertions.assertEquals(0, cardRepository.findAll().size());
        Assertions.assertEquals(0, cardHolderRepository.findAll().size());
    }

    @Step("Process request")
    @Attachment("Payment response")
    private PaymentResponse processExchange(PaymentServiceGrpc.PaymentServiceBlockingStub stub, PaymentRequest request) {
        return stub.createPayment(request);
    }
}