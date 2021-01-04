package net.example.server.core.validation;

import com.google.common.base.Objects;
import net.example.server.api.service.ICardService;
import net.example.server.api.to.CardHolderTo;
import net.example.server.api.to.CardTo;
import net.example.server.api.to.CreateCardPaymentTo;
import net.example.server.api.to.PaymentTo;
import net.example.server.api.validation.ICreateCardTransactionValidator;
import net.example.server.api.validation.ValidationResult;
import net.example.server.core.validation.util.CardNumberValidator;
import net.example.server.core.validation.util.NotBlankStringValidator;
import net.example.server.core.validation.util.NotNullValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateCardTransactionValidator implements ICreateCardTransactionValidator {

    @Autowired
    private ICardService cardService;

    @Override
    public ValidationResult validate(CreateCardPaymentTo createCardPayment) {
        ValidationResult result = new ValidationResult();
        NotNullValidator.validate(result, createCardPayment, CreateCardPaymentTo.class);
        validateCreateCardPaymentTo(createCardPayment, result);

        CardTo requestCard = createCardPayment.getPayment().getCard();
        CardTo card = cardService.findCardWithCardHolderByCardNumber(requestCard);
        if (createCardPayment.getPayment() != null &&
                createCardPayment.getPayment().getCard() != null &&
                createCardPayment.getPayment().getCard().getCardHolder() != null) {
            CardHolderTo cardHolder = createCardPayment.getPayment().getCard().getCardHolder();
            if (!isCardCompliantWithRequestCardHolder(card, cardHolder)) {
                result.addValidationError("Credit card has already assigned different card holder");
            }
        }
        return result;
    }

    private void validateCreateCardPaymentTo(CreateCardPaymentTo createCardPayment, ValidationResult result) {
        if (createCardPayment != null) {
            PaymentTo payment = createCardPayment.getPayment();
            NotNullValidator.validate(result, payment, PaymentTo.class);
            validatePaymentTo(result, payment);
        }
    }

    private void validatePaymentTo(ValidationResult result, PaymentTo payment) {
        if (payment != null) {
            CardTo card = payment.getCard();
            BigDecimal amount = payment.getAmount();
            NotNullValidator.validate(result, card, CardTo.class);
            NotNullValidator.validate(result, amount, "payment amount");
            validateCardTo(result, card);
        }
    }

    private void validateCardTo(ValidationResult result, CardTo card) {
        if (card != null) {
            CardHolderTo cardHolder = card.getCardHolder();
            String cardNumber = card.getCardNumber();
            NotNullValidator.validate(result, cardHolder, CardHolderTo.class);
            NotBlankStringValidator.validate(result, cardNumber, "card number");
            CardNumberValidator.validate(result, cardNumber);
            ValidateCardHOlder(result, cardHolder);
        }
    }

    private void ValidateCardHOlder(ValidationResult result, CardHolderTo cardHolder) {
        if (cardHolder != null) {
            NotBlankStringValidator.validate(result, cardHolder.getFirstName(), "firstName");
            NotBlankStringValidator.validate(result, cardHolder.getLastName(), "lastName");
        }
    }

    private boolean isCardCompliantWithRequestCardHolder(CardTo card, CardHolderTo cardHolder) {
        if (card == null || card.getCardHolder() == null ||
                isSameCardHolderByFullName(card.getCardHolder(), cardHolder)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isSameCardHolderByFullName(CardHolderTo h1, CardHolderTo h2) {
        return Objects.equal(h1.getFirstName(), h2.getFirstName()) &&
                Objects.equal(h1.getLastName(), h2.getLastName());
    }
}
