package net.example.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.example.server.api.service.IPaymentService;
import net.example.server.api.to.PaymentTo;
import net.example.server.jpa.entity.Card;
import net.example.server.jpa.entity.CardHolder;
import net.example.server.jpa.entity.Payment;
import net.example.server.jpa.mapping.CardHolderMapper;
import net.example.server.jpa.mapping.CardMapper;
import net.example.server.jpa.mapping.PaymentMapper;
import net.example.server.jpa.mapping.util.CycleAvoidingMappingContext;
import net.example.server.jpa.repository.CardHolderRepository;
import net.example.server.jpa.repository.CardRepository;
import net.example.server.jpa.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService implements IPaymentService {

    @Autowired
    private CardHolderRepository cardHolderRepository;

    @Autowired
    private CardHolderMapper cardHolderMapper;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public PaymentTo createPaymentWithNewCardAndNewCardHolder(PaymentTo paymentTo) {
        CardHolder cardHolder = cardHolderMapper.mapCardHolder(paymentTo.getCard().getCardHolder(),
                new CycleAvoidingMappingContext());
        cardHolder = cardHolderRepository.save(cardHolder);
        Card card = cardMapper.mapCard(paymentTo.getCard(), new CycleAvoidingMappingContext());
        card.setCardHolder(cardHolder);
        card = cardRepository.save(card);
        Payment payment = paymentMapper.mapPayment(paymentTo, new CycleAvoidingMappingContext());
        payment.setCard(card);
        payment = paymentRepository.save(payment);
        return paymentMapper.mapPaymentTo(payment, new CycleAvoidingMappingContext());
    }

    @Override
    public PaymentTo createPaymentWithExistingCardAndNewCardHolder(PaymentTo paymentTo) {
        CardHolder cardHolder = cardHolderMapper.mapCardHolder(paymentTo.getCard().getCardHolder(),
                new CycleAvoidingMappingContext());
        cardHolder = cardHolderRepository.save(cardHolder);
        Card card = cardRepository.getOne(paymentTo.getCard().getId());
        card.setCardHolder(cardHolder);
        card = cardRepository.save(card);
        Payment payment = paymentMapper.mapPayment(paymentTo, new CycleAvoidingMappingContext());
        payment.setCard(card);
        payment = paymentRepository.save(payment);
        return paymentMapper.mapPaymentTo(payment, new CycleAvoidingMappingContext());
    }

    @Override
    public PaymentTo createPaymentWithNewCardAndExistingCardHolder(PaymentTo paymentTo) {
        CardHolder cardHolder = cardHolderRepository.getOne(paymentTo.getCard().getCardHolder().getId());
        Card card = cardMapper.mapCard(paymentTo.getCard(), new CycleAvoidingMappingContext());
        card.setCardHolder(cardHolder);
        card = cardRepository.save(card);
        Payment payment = paymentMapper.mapPayment(paymentTo, new CycleAvoidingMappingContext());
        payment.setCard(card);
        payment = paymentRepository.save(payment);
        return paymentMapper.mapPaymentTo(payment, new CycleAvoidingMappingContext());
    }

    @Override
    public PaymentTo createPaymentWithExistingCard(PaymentTo paymentTo) {
        Card card = cardRepository.getOne(paymentTo.getCard().getId());
        Payment payment = paymentMapper.mapPayment(paymentTo, new CycleAvoidingMappingContext());
        payment.setCard(card);
        payment = paymentRepository.save(payment);
        return paymentMapper.mapPaymentTo(payment, new CycleAvoidingMappingContext());
    }
}
