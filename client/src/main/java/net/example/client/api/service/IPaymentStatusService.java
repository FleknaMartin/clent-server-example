package net.example.client.api.service;

import net.example.client.jpa.entity.PaymentStatus;

public interface IPaymentStatusService {

    void postUpdate(PaymentStatus paymentStatus);
}
