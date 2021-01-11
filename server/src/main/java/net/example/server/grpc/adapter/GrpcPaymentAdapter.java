package net.example.server.grpc.adapter;

import lombok.extern.slf4j.Slf4j;
import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import net.example.server.api.servicefacade.ICardPaymentServiceFacade;
import net.example.server.api.to.CreateCardPaymentResultTo;
import net.example.server.api.to.CreateCardPaymentTo;
import net.example.server.grpc.mapping.PaymentServiceEndpointMapper;
import net.example.server.grpc.utils.SyncResponseExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GrpcPaymentAdapter {

    @Autowired
    private PaymentServiceEndpointMapper mapper;

    @Autowired
    private ICardPaymentServiceFacade cardTransactionServiceFacade;

    public PaymentResponse createPayment(PaymentRequest request){
        CreateCardPaymentTo createCardTransaction = mapper.mapCreateCardTransactionTo(request);
        CreateCardPaymentResultTo result = cardTransactionServiceFacade.createCardPayment(createCardTransaction);
        return mapper.mapPaymentResponse(result);
    }

    @Async
    public void createPaymentAsync(PaymentRequest request, SyncResponseExecutor responseExecutor){
        CreateCardPaymentTo createCardTransaction = mapper.mapCreateCardTransactionTo(request);
        CreateCardPaymentResultTo result = cardTransactionServiceFacade.createCardPayment(createCardTransaction);
        PaymentResponse response =  mapper.mapPaymentResponse(result);
        responseExecutor.sendResponse(response);
    }

}
