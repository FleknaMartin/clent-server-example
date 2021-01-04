package net.example.server.integration;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import net.example.grpc.BigDecimal;
import net.example.grpc.PaymentRequest;
import net.example.server.jpa.entity.Card;
import net.example.server.jpa.entity.CardHolder;
import net.example.server.jpa.entity.Payment;
import net.example.server.jpa.repository.CardHolderRepository;
import net.example.server.jpa.repository.CardRepository;
import net.example.server.jpa.repository.PaymentRepository;
import net.example.server.util.BasicAuthenticationCallCredentials;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.stream.Collectors;

import static net.example.server.util.TestConstants.*;

public abstract class BaseIntegrationTest {

    @Value("${grpc.port}")
    Integer port;

    @Value("serverCert.pem")
    ClassPathResource certResource;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardHolderRepository cardHolderRepository;

    ManagedChannel channel;

    CallCredentials credentials;

    @BeforeEach
    @Step("Init channel")
    public void init() throws Exception {
        SslContext sslContext = GrpcSslContexts.forClient()
                .trustManager(certResource.getInputStream())
                .build();
        channel = NettyChannelBuilder.forAddress(LOCALHOST, port)
                .sslContext(sslContext)
                .build();
        credentials = new BasicAuthenticationCallCredentials(USER, PASSWORD);
    }

    @Step("Create payment")
    protected PaymentRequest createPayment(String firstName, String lastName, BigDecimal amount, String cardNumber) {
        return PaymentRequest.newBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAmount(amount)
                .setCardNumber(cardNumber)
                .build();
    }

    @AfterEach
    @Step("After test")
    public void after() {
        channel.shutdown();
        printAllData();
    }

    @Step("Get all payment")
    @Attachment()
    public String getAllPayments() {
        return printList(paymentRepository.findAll(), Payment.class);
    }

    @Step("Get all cards")
    @Attachment()
    public String getAllCards() {
        return printList(cardRepository.findAll(), Card.class);
    }

    @Step("Get all cardHolders")
    @Attachment()
    public String getAllCardHolderss() {
        return printList(cardHolderRepository.findAll(), CardHolder.class);
    }

    private <T> String printList(List<T> list, Class<T> tClass) {
        return list.stream()
                .map(t -> ToStringBuilder.reflectionToString(t))
                .collect(Collectors.joining("\n"));
    }

    @Step("Print all data")
    private void printAllData() {
        getAllCardHolderss();
        getAllCards();
        getAllPayments();
    }
}
