package net.example.client.api.servicefacade;

import net.example.client.api.enums.CreatePaymentResult;
import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;

import java.util.List;

public interface IPaymentServiceFacade {

    CreatePaymentResultTo processPayment(CreatePaymentTo payment);

    void reprocessStoredPayments(List<CreatePaymentResult> results);

}
