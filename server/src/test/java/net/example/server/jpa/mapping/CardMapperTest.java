package net.example.server.jpa.mapping;

import net.example.server.api.to.CardHolderTo;
import net.example.server.api.to.CardTo;
import net.example.server.api.to.PaymentTo;
import net.example.server.jpa.entity.Card;
import net.example.server.jpa.entity.CardHolder;
import net.example.server.jpa.mapping.config.TestConfig;
import net.example.server.jpa.mapping.util.CycleAvoidingMappingContext;
import net.example.server.util.TestDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = TestConfig.class)
class CardMapperTest {

    @Autowired
    private CardMapper mapper;

    @Test
    public void mapCardTest_Success(){
        CardTo cardTo = TestDataProvider.getDefaultTo(CardTo.class);

        Card card = mapper.mapCard(cardTo, new CycleAvoidingMappingContext());

        assertEquals(cardTo.getId(), card.getId());
        assertEquals(cardTo.getCardNumber(), card.getCardNumber());
        CardHolderTo cardHolderTo = cardTo.getCardHolder();
        assertEquals(cardHolderTo.getId(), card.getCardHolder().getId());
        assertEquals(cardHolderTo.getFirstName(), card.getCardHolder().getFirstName());
        assertEquals(cardHolderTo.getLastName(), card.getCardHolder().getLastName());
        assertEquals(1, card.getPayments().size());
        PaymentTo paymentTo = cardTo.getPayments().get(0);
        assertEquals(paymentTo.getId(), card.getPayments().get(0).getId());
        assertEquals(paymentTo.getAmount(), card.getPayments().get(0).getAmount());
        assertEquals(paymentTo.getPaymentTime(), card.getPayments().get(0).getPaymentTime());
    }

    @Test
    public void mapCardToTest_Success(){
        Card card = TestDataProvider.getDefault(Card.class);

        CardTo cardTo = mapper.mapCardTo(card, new CycleAvoidingMappingContext());

        assertEquals(card.getId(), cardTo.getId());
        assertEquals(card.getCardNumber(), cardTo.getCardNumber());
        CardHolder cardHolder = card.getCardHolder();
        assertEquals(cardHolder.getId(), cardTo.getCardHolder().getId());
        assertEquals(cardHolder.getFirstName(), cardTo.getCardHolder().getFirstName());
        assertEquals(cardHolder.getLastName(), cardTo.getCardHolder().getLastName());
        assertNull(cardTo.getPayments());
    }
}