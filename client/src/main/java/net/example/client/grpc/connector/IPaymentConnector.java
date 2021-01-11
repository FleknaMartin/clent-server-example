package net.example.client.grpc.connector;

import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;

public interface IPaymentConnector {

    PaymentResponse sendRequest(PaymentRequest request);
}
