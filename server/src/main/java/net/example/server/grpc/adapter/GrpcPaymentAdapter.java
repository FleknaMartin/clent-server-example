package net.example.server.grpc.adapter;

import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import net.example.server.api.servicefacade.ICardPaymentServiceFacade;
import net.example.server.api.to.CreateCardPaymentResultTo;
import net.example.server.api.to.CreateCardPaymentTo;
import net.example.server.grpc.mapping.PaymentServiceEndpointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrpcPaymentAdapter {

    @Autowired
    private PaymentServiceEndpointMapper mapper;

    @Autowired
    private ICardPaymentServiceFacade cardTransactionServiceFacade;

    public PaymentResponse createPayment(PaymentRequest request){
        CreateCardPaymentTo createCardTransaction = mapper.CreateCardTransactionToMapper(request);
        CreateCardPaymentResultTo result = cardTransactionServiceFacade.createCardPayment(createCardTransaction);
        return mapper.paymentResponseMapper(result);
    }

}
