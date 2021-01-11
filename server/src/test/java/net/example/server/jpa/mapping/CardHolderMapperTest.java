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
import org.junit.jupiter.api.Test;
import org.lognet.springboot.grpc.security.GrpcSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = TestConfig.class)
class CardHolderMapperTest {

    @Autowired
    private CardHolderMapper mapper;

    @Test
    public void mapCardHolderTest_Success() {
        CardHolderTo cardHolderTo = TestDataProvider.getDefaultTo(CardHolderTo.class);

        CardHolder cardHolder = mapper.mapCardHolder(cardHolderTo, new CycleAvoidingMappingContext());

        assertEquals(cardHolderTo.getId(), cardHolder.getId());
        assertEquals(cardHolderTo.getFirstName(), cardHolder.getFirstName());
        assertEquals(cardHolderTo.getLastName(), cardHolder.getLastName());
        assertEquals(1, cardHolder.getCards().size());
        Card card = cardHolder.getCards().get(0);
        CardTo cardTo = cardHolderTo.getCards().get(0);
        assertEquals(cardTo.getId(), card.getId());
        assertEquals(cardTo.getCardNumber(), card.getCardNumber());
        List<Payment> payments = card.getPayments();
        assertEquals(1, payments.size());
        Payment payment = payments.get(0);
        PaymentTo paymentTo = cardTo.getPayments().get(0);
        assertEquals(paymentTo.getId(), payment.getId());
        assertEquals(paymentTo.getAmount(), payment.getAmount());
        assertEquals(paymentTo.getPaymentTime(), payment.getPaymentTime());
    }

    @Test
    public void mapCardHolderToTest_Success() {
        CardHolder cardHolder = TestDataProvider.getDefault(CardHolder.class);

        CardHolderTo cardHolderTo = mapper.mapCardHolderTo(cardHolder, new CycleAvoidingMappingContext());

        assertEquals(cardHolder.getId(), cardHolderTo.getId());
        assertEquals(cardHolder.getFirstName(), cardHolderTo.getFirstName());
        assertEquals(cardHolder.getLastName(), cardHolderTo.getLastName());
        assertEquals(1, cardHolderTo.getCards().size());
        CardTo cardTo = cardHolderTo.getCards().get(0);
        Card card = cardHolder.getCards().get(0);
        assertEquals(card.getId(), cardTo.getId());
        assertEquals(card.getCardNumber(), cardTo.getCardNumber());
        assertNull(cardTo.getPayments());
    }
}