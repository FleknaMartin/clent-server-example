package net.example.server.api.service;

import net.example.server.api.to.PaymentTo;

public interface IPaymentService {
    PaymentTo createPaymentWithNewCardAndNewCardHolder(PaymentTo payment);

    PaymentTo createPaymentWithExistingCardAndNewCardHolder(PaymentTo payment);

    PaymentTo createPaymentWithNewCardAndExistingCardHolder(PaymentTo payment);

    PaymentTo createPaymentWithExistingCard(PaymentTo payment);
}
