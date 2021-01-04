package net.example.server.jpa.mapping;

import net.example.server.api.to.CardHolderTo;
import net.example.server.api.to.CardTo;
import net.example.server.api.to.PaymentTo;
import net.example.server.jpa.entity.Card;
import net.example.server.jpa.entity.CardHolder;
import net.example.server.jpa.entity.Payment;
import net.example.server.jpa.mapping.config.TestConfig;
import net.example.server.jpa.mapping.util.CycleAvoidingMappingContext;
import net.example.server.util.TestDataProvider;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TestConfig.class)
class PaymentMapperTest {

    @Autowired
    private PaymentMapper mapper;

    @Test
    public void mapPaymentTest_Success(){
        PaymentTo paymentTo = TestDataProvider.getDefaultTo(PaymentTo.class);

        Payment payment = mapper.mapPayment(paymentTo, new CycleAvoidingMappingContext());

        assertEquals(paymentTo.getId(), payment.getId());
        assertEquals(paymentTo.getAmount(), payment.getAmount());
        assertEquals(paymentTo.getPaymentTime(), payment.getPaymentTime());
        CardTo cardTo = paymentTo.getCard();
        assertEquals(cardTo.getId(), payment.getCard().getId());
        assertEquals(cardTo.getCardNumber(), payment.getCard().getCardNumber());
        CardHolderTo cardHolderTo = cardTo.getCardHolder();
        assertEquals(cardHolderTo.getId(), payment.getCard().getCardHolder().getId());
        assertEquals(cardHolderTo.getFirstName(), payment.getCard().getCardHolder().getFirstName());
        assertEquals(cardHolderTo.getLastName(), payment.getCard().getCardHolder().getLastName());
    }

    @Test
    public void mapPaymentToTest_Success(){
        Payment payment = TestDataProvider.getDefault(Payment.class);

        PaymentTo paymentTo = mapper.mapPaymentTo(payment, new CycleAvoidingMappingContext());

        assertEquals(payment.getId(), paymentTo.getId());
        assertEquals(payment.getAmount(), paymentTo.getAmount());
        assertEquals(payment.getPaymentTime(), paymentTo.getPaymentTime());
        Card card = payment.getCard();
        assertEquals(card.getId(), paymentTo.getCard().getId());
        assertEquals(card.getCardNumber(), paymentTo.getCard().getCardNumber());
        CardHolder cardHolder = card.getCardHolder();
        assertEquals(cardHolder.getId(), paymentTo.getCard().getCardHolder().getId());
        assertEquals(cardHolder.getFirstName(), payment.getCard().getCardHolder().getFirstName());
        assertEquals(cardHolder.getLastName(), paymentTo.getCard().getCardHolder().getLastName());
    }
}