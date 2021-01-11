package net.example.client.websocket.adapter;

import net.example.client.api.servicefacade.IPaymentServiceFacade;
import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.websocket.mapping.PaymentWsMapper;
import net.example.client.websocket.model.CreatePaymentWsMessage;
import net.example.client.websocket.model.PaymentStatusWsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceFacadeAdapter {

    @Autowired
    private PaymentWsMapper mapper;

    @Autowired
    private IPaymentServiceFacade paymentServiceFacade;

    public PaymentStatusWsMessage processPayment(CreatePaymentWsMessage message, String userId) {
        CreatePaymentResultTo result = paymentServiceFacade.processPayment(mapper.mapCreatePaymentTo(message, userId));
        return mapper.mapPaymentStatusWsMessage(result);
    }
}
