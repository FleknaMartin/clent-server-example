package net.example.server.util;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import net.example.server.api.to.CardHolderTo;
import net.example.server.api.to.CardTo;
import net.example.server.api.to.PaymentTo;
import net.example.server.jpa.entity.Card;
import net.example.server.jpa.entity.CardHolder;
import net.example.server.jpa.entity.Payment;

import java.sql.PreparedStatement;
import java.util.Collections;

import static net.example.server.util.TestConstants.*;
import static net.example.server.util.TestConstants.ZONED_DATE_TIME;

public class TestDataProvider {

    @Step("Prepare data")
    @Attachment("Prepared To for mapping")
    public static <T> T getDefaultTo(Class<T> t) {
        CardHolderTo cardHolderTo = new CardHolderTo();
        cardHolderTo.setId(ID_1);
        cardHolderTo.setFirstName(FIRST_NAME);
        cardHolderTo.setLastName(LAST_NAME);
        CardTo cardTo = new CardTo();
        cardTo.setId(ID_1);
        cardTo.setCardNumber(CARD_NUMBER);
        cardTo.setCardHolder(cardHolderTo);
        cardHolderTo.setCards(Collections.singletonList(cardTo));
        PaymentTo paymentTo = new PaymentTo();
        paymentTo.setId(ID_1);
        paymentTo.setAmount(BIG_DECIMAL);
        paymentTo.setPaymentTime(ZONED_DATE_TIME);
        paymentTo.setCard(cardTo);
        cardTo.setPayments(Collections.singletonList(paymentTo));

        if(t.getCanonicalName().equals(CardHolderTo.class.getCanonicalName())){
            return t.cast(cardHolderTo);
        } else if(t.getCanonicalName().equals(CardTo.class.getCanonicalName())){
            return t.cast(cardTo);
        } else if (t.getCanonicalName().equals(PaymentTo.class.getCanonicalName())){
            return t.cast(paymentTo);
        } else {
            throw new RuntimeException("Unsupported type: " + t.getClass());
        }
    }

    @Step("Prepare data")
    @Attachment("Prepared Entity for mapping")
    public static <T> T getDefault(Class<T> t) {
        CardHolder cardHolder = new CardHolder();
        cardHolder.setId(ID_1);
        cardHolder.setFirstName(FIRST_NAME);
        cardHolder.setLastName(LAST_NAME);
        Card card = new Card();
        card.setId(ID_1);
        card.setCardNumber(CARD_NUMBER);
        card.setCardHolder(cardHolder);
        cardHolder.setCards(Collections.singletonList(card));
        Payment payment = new Payment();
        payment.setId(ID_1);
        payment.setAmount(BIG_DECIMAL);
        payment.setPaymentTime(ZONED_DATE_TIME);
        payment.setCard(card);
        card.setPayments(Collections.singletonList(payment));

        if(t.getCanonicalName().equals(CardHolder.class.getCanonicalName())){
            return t.cast(cardHolder);
        } else if(t.getCanonicalName().equals(Card.class.getCanonicalName())){
            return t.cast(card);
        } else if (t.getCanonicalName().equals(Payment.class.getCanonicalName())){
            return t.cast(payment);
        } else {
            throw new RuntimeException("Unsupported type: " + t.getClass());
        }
    }
}
