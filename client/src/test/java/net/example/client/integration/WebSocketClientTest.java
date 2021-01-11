package net.example.client.integration;

import lombok.extern.slf4j.Slf4j;
import net.example.client.api.enums.CreatePaymentResult;
import net.example.client.integration.simulation.PaymentConnectorSimulator;
import net.example.client.integration.client.WebSocketClientBaseTest;
import net.example.client.job.ProcessStoredPaymentsJob;
import net.example.client.websocket.model.CreatePaymentWsMessage;
import net.example.client.websocket.model.PaymentStatusWsMessage;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.concurrent.TimeUnit;

import static net.example.client.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class WebSocketClientTest extends WebSocketClientBaseTest {

    @Autowired
    PaymentConnectorSimulator paymentConnectorSimulator;

    @Test
    public void createPayment_success() {
        paymentConnectorSimulator.setReply(PaymentConnectorSimulator.Reply.SUCCESS);

        CreatePaymentWsMessage message = new CreatePaymentWsMessage();
        message.setFirstName(FIRST_NAME);
        message.setLastName(LAST_NAME);
        message.setCardNumber(CARD_NUMBER);
        message.setAmount(AMOUNT);

        session.send("/app/payments", message);
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> responses.size() == 1);
        PaymentStatusWsMessage result = responses.get(0);

        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertEquals(CARD_NUMBER, result.getCardNumber());
        assertEquals(AMOUNT, result.getAmount());
        assertEquals(CreatePaymentResult.CREATED, result.getResult());
        org.assertj.core.api.Assertions.assertThat(result.getMessage()).isBlank();
    }

    @Test
    public void createPayment_failure() {
        paymentConnectorSimulator.setReply(PaymentConnectorSimulator.Reply.FAILED);

        CreatePaymentWsMessage message = new CreatePaymentWsMessage();
        message.setFirstName(FIRST_NAME);
        message.setLastName(LAST_NAME);
        message.setCardNumber(CARD_NUMBER);
        message.setAmount(AMOUNT);

        session.send("/app/payments", message);
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> responses.size() == 1);
        PaymentStatusWsMessage result = responses.get(0);

        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertEquals(CARD_NUMBER, result.getCardNumber());
        assertEquals(AMOUNT, result.getAmount());
        assertEquals(CreatePaymentResult.FAILED_SERVER, result.getResult());
        assertEquals(MESSAGE_SIMULATION_FAILURE, result.getMessage());
    }

    @Test
    public void createPayment_exception() {
        paymentConnectorSimulator.setReply(PaymentConnectorSimulator.Reply.EXCEPTION);

        CreatePaymentWsMessage message = new CreatePaymentWsMessage();
        message.setFirstName(FIRST_NAME);
        message.setLastName(LAST_NAME);
        message.setCardNumber(CARD_NUMBER);
        message.setAmount(AMOUNT);

        session.send("/app/payments", message);
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> responses.size() == 1);
        PaymentStatusWsMessage result = responses.get(0);

        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertEquals(CARD_NUMBER, result.getCardNumber());
        assertEquals(AMOUNT, result.getAmount());
        assertEquals(CreatePaymentResult.ERROR, result.getResult());
        assertEquals(MESSAGE_SIMULATION_EXCEPTION, result.getMessage());
    }

    @Test
    public void createPayment_unavailable() {
        paymentConnectorSimulator.setReply(PaymentConnectorSimulator.Reply.UNAVAILABLE);

        CreatePaymentWsMessage message = new CreatePaymentWsMessage();
        message.setFirstName(FIRST_NAME);
        message.setLastName(LAST_NAME);
        message.setCardNumber(CARD_NUMBER);
        message.setAmount(AMOUNT);

        session.send("/app/payments", message);
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> responses.size() == 1);
        PaymentStatusWsMessage result = responses.get(0);

        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertEquals(CARD_NUMBER, result.getCardNumber());
        assertEquals(AMOUNT, result.getAmount());
        assertEquals(CreatePaymentResult.PENDING, result.getResult());
        assertEquals(PaymentConnectorSimulator.Reply.UNAVAILABLE.name(), result.getMessage());

        paymentConnectorSimulator.setReply(PaymentConnectorSimulator.Reply.SUCCESS);

        TriggerBuilder.newTrigger()
                .forJob(ProcessStoredPaymentsJob.class.getName())
                .startNow();

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> responses.size() == 2);

        result = responses.get(1);

        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertEquals(CARD_NUMBER, result.getCardNumber());
        assertEquals(AMOUNT, result.getAmount());
        assertEquals(CreatePaymentResult.CREATED, result.getResult());
        org.assertj.core.api.Assertions.assertThat(result.getMessage()).isBlank();
    }
}
