package net.example.client.api.service;

import net.example.client.api.enums.CreatePaymentResult;
import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;
import net.example.client.jpa.entity.PaymentStatus;

import java.util.List;

public interface IPaymentService {
    CreatePaymentResultTo processSinglePayment(CreatePaymentTo request);

    void reprocessSinglePaymentAsync(CreatePaymentTo request);

    PaymentStatus persistPayment(CreatePaymentTo payment, CreatePaymentResultTo result);

    List<CreatePaymentTo> loadPaymentsInStatus(List<CreatePaymentResult> results);
}
