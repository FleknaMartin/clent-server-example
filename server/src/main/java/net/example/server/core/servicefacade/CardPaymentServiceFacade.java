package net.example.server.core.servicefacade;

import lombok.extern.slf4j.Slf4j;
import net.example.server.api.enm.CreateCardTransactionResult;
import net.example.server.api.service.ICardHolderService;
import net.example.server.api.service.ICardService;
import net.example.server.api.service.IPaymentService;
import net.example.server.api.servicefacade.ICardPaymentServiceFacade;
import net.example.server.api.to.*;
import net.example.server.api.validation.ICreateCardTransactionValidator;
import net.example.server.api.validation.ValidationResult;
import net.example.server.jpa.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CardPaymentServiceFacade implements ICardPaymentServiceFacade {

    @Autowired
    private ICardHolderService cardHolderService;

    @Autowired
    private ICardService cardService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private ICreateCardTransactionValidator createCardTransactionValidator;

    @Override
    public CreateCardPaymentResultTo createCardPayment(CreateCardPaymentTo createCardPayment) {

        ValidationResult validationResult = createCardTransactionValidator.validate(createCardPayment);
        CreateCardPaymentResultTo result = new CreateCardPaymentResultTo();

        if (validationResult.isValid()) {
            CardTo requestCard = createCardPayment.getPayment().getCard();
            CardTo card = cardService.findCardWithCardHolderByCardNumber(requestCard);

            CardHolderTo requestCardHolder = createCardPayment.getPayment().getCard().getCardHolder();
            CardHolderTo cardHolder = cardHolderService.findCardHolder(requestCardHolder);

            PaymentTo requestPayment = createCardPayment.getPayment();
            PaymentTo newPayment;
            if (card == null && cardHolder == null) {
                newPayment = paymentService.createPaymentWithNewCardAndNewCardHolder(requestPayment);
            } else if (card != null && cardHolder == null) {
                card.setCardHolder(requestCardHolder);
                requestPayment.setCard(card);
                newPayment = paymentService.createPaymentWithExistingCardAndNewCardHolder(requestPayment);
            } else if (card == null) {
                requestCard.setCardHolder(cardHolder);
                newPayment = paymentService.createPaymentWithNewCardAndExistingCardHolder(requestPayment);
            } else {
                card.setCardHolder(cardHolder);
                requestPayment.setCard(card);
                newPayment = paymentService.createPaymentWithExistingCard(requestPayment);
            }
            result.setResult(CreateCardTransactionResult.SUCCESS);
            result.setPayment(newPayment);
        } else {
            result.setResult(CreateCardTransactionResult.FAILURE);
            result.setMessage(validationResult.getValidationErrors().stream()
                    .map(m -> m.getValidationErrorMessage())
                    .collect(Collectors.joining(";")));
        }
        return result;
    }
}
