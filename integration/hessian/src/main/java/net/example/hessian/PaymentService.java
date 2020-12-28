package net.example.hessian;

import net.example.hessian.message.PaymentRequest;
import net.example.hessian.message.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest paymentRequest);
}
