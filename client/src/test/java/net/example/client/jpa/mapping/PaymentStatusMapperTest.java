package net.example.client.jpa.mapping;

import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;
import net.example.client.jpa.entity.PaymentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static net.example.client.utils.TestConstants.*;

class PaymentStatusMapperTest {

    private PaymentStatusMapper mapper = Mappers.getMapper(PaymentStatusMapper.class);

    @Test
    public void mapPaymentStatus_Success() {
        CreatePaymentTo payment = new CreatePaymentTo();
        payment.setId(ID);
        payment.setFirstName(FIRST_NAME);
        payment.setLastName(LAST_NAME);
        payment.setCardNumber(CARD_NUMBER);
        payment.setAmount(AMOUNT);
        payment.setPaymentTime(PAYMENT_TIME);
        payment.setUserId(USER_ID);

        CreatePaymentResultTo result = new CreatePaymentResultTo();
        result.setResult(RESULT);
        result.setMessage(MESSAGE);

        PaymentStatus status = mapper.mapPaymentStatus(payment, result);
        Assertions.assertEquals(FIRST_NAME, status.getFirstName());
        Assertions.assertEquals(LAST_NAME, status.getLastName());
        Assertions.assertEquals(CARD_NUMBER, status.getCardNumber());
        Assertions.assertEquals(AMOUNT, status.getAmount());
        Assertions.assertEquals(ID, status.getId());
        Assertions.assertEquals(RESULT, status.getResult());
        Assertions.assertEquals(MESSAGE, status.getMessage());
    }

    @Test
    public void mapCreatePaymentTo_Success() {
        PaymentStatus payment = new PaymentStatus();
        payment.setId(ID);
        payment.setFirstName(FIRST_NAME);
        payment.setLastName(LAST_NAME);
        payment.setCardNumber(CARD_NUMBER);
        payment.setAmount(AMOUNT);
        payment.setPaymentTime(PAYMENT_TIME);
        payment.setUserId(USER_ID);

        CreatePaymentTo result = mapper.mapCreatePaymentTo(payment);
        Assertions.assertEquals(FIRST_NAME, result.getFirstName());
        Assertions.assertEquals(LAST_NAME, result.getLastName());
        Assertions.assertEquals(CARD_NUMBER, result.getCardNumber());
        Assertions.assertEquals(AMOUNT, result.getAmount());
        Assertions.assertEquals(ID, result.getId());
    }

    @Test
    public void mapCreatePaymentToList_Success() {
        PaymentStatus payment = new PaymentStatus();
        payment.setId(ID);
        payment.setFirstName(FIRST_NAME);
        payment.setLastName(LAST_NAME);
        payment.setCardNumber(CARD_NUMBER);
        payment.setAmount(AMOUNT);
        payment.setPaymentTime(PAYMENT_TIME);
        payment.setUserId(USER_ID);

        List<CreatePaymentTo> results = mapper.mapCreatePaymentToList(Collections.singletonList(payment));
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(FIRST_NAME, results.get(0).getFirstName());
        Assertions.assertEquals(LAST_NAME, results.get(0).getLastName());
        Assertions.assertEquals(CARD_NUMBER, results.get(0).getCardNumber());
        Assertions.assertEquals(AMOUNT, results.get(0).getAmount());
        Assertions.assertEquals(ID, results.get(0).getId());
    }
}