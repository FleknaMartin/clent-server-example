package net.example.client.websocket.mapper;

import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;
import net.example.client.jpa.entity.PaymentStatus;
import net.example.client.websocket.mapping.PaymentWsMapper;
import net.example.client.websocket.model.CreatePaymentWsMessage;
import net.example.client.websocket.model.PaymentStatusWsMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static net.example.client.utils.TestConstants.*;

public class PaymentWsMapperTest {

    private PaymentWsMapper mapper = Mappers.getMapper(PaymentWsMapper.class);

    @Test
    public void mapCreatePaymentTo2PaymentStatusWsMessage_Success(){
        CreatePaymentTo paymentTo = new CreatePaymentTo();
        paymentTo.setId(ID);
        paymentTo.setFirstName(FIRST_NAME);
        paymentTo.setLastName(LAST_NAME);
        paymentTo.setCardNumber(CARD_NUMBER);
        paymentTo.setAmount(AMOUNT);
        paymentTo.setPaymentTime(PAYMENT_TIME);
        CreatePaymentResultTo resultTo = new CreatePaymentResultTo();
        resultTo.setPayment(paymentTo);
        resultTo.setResult(RESULT);
        resultTo.setMessage(MESSAGE);

        PaymentStatusWsMessage message = mapper.mapPaymentStatusWsMessage(resultTo);
        Assertions.assertEquals(FIRST_NAME, message.getFirstName());
        Assertions.assertEquals(LAST_NAME, message.getLastName());
        Assertions.assertEquals(CARD_NUMBER, message.getCardNumber());
        Assertions.assertEquals(AMOUNT, message.getAmount());
        Assertions.assertEquals(ID, message.getId());
        Assertions.assertEquals(RESULT, message.getResult());
        Assertions.assertEquals(MESSAGE, message.getMessage());
    }

    @Test
    public void mapPaymentStatus2PaymentStatusWsMessage_Success(){
        PaymentStatus paymentStatus = new PaymentStatus();
        paymentStatus.setId(ID);
        paymentStatus.setFirstName(FIRST_NAME);
        paymentStatus.setLastName(LAST_NAME);
        paymentStatus.setCardNumber(CARD_NUMBER);
        paymentStatus.setAmount(AMOUNT);
        paymentStatus.setResult(RESULT);
        paymentStatus.setMessage(MESSAGE);

        PaymentStatusWsMessage message = mapper.mapPaymentStatusWsMessage(paymentStatus);
        Assertions.assertEquals(FIRST_NAME, message.getFirstName());
        Assertions.assertEquals(LAST_NAME, message.getLastName());
        Assertions.assertEquals(CARD_NUMBER, message.getCardNumber());
        Assertions.assertEquals(AMOUNT, message.getAmount());
        Assertions.assertEquals(ID, message.getId());
        Assertions.assertEquals(RESULT, message.getResult());
        Assertions.assertEquals(MESSAGE, message.getMessage());
    }

    @Test
    public void mapCreatePaymentTo_Success(){
        CreatePaymentWsMessage message = new CreatePaymentWsMessage();
        message.setFirstName(FIRST_NAME);
        message.setLastName(LAST_NAME);
        message.setCardNumber(CARD_NUMBER);
        message.setAmount(AMOUNT);

        CreatePaymentTo payment = mapper.mapCreatePaymentTo(message, USER_ID);
        Assertions.assertEquals(FIRST_NAME, payment.getFirstName());
        Assertions.assertEquals(LAST_NAME, payment.getLastName());
        Assertions.assertEquals(CARD_NUMBER, payment.getCardNumber());
        Assertions.assertEquals(AMOUNT, payment.getAmount());
        Assertions.assertEquals(USER_ID, payment.getUserId());
    }

}