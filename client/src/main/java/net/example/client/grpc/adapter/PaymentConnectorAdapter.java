package net.example.client.grpc.adapter;

import net.example.client.api.adapter.IPaymentServiceConnectorAdapter;
import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;
import net.example.client.grpc.connector.IPaymentConnector;
import net.example.client.grpc.mapping.PaymentConnectorMapper;
import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentConnectorAdapter implements IPaymentServiceConnectorAdapter {

    @Autowired
    private PaymentConnectorMapper mapper;

    @Autowired
    private IPaymentConnector paymentConnector;

    @Override
    public CreatePaymentResultTo send(CreatePaymentTo payment) {
        PaymentRequest request = mapper.PaymentRequestMapper(payment);
        PaymentResponse response = paymentConnector.sendRequest(request);
        return mapper.paymentResponseMapper(response);
    }
}
