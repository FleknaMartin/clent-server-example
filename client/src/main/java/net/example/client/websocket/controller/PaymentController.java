package net.example.client.websocket.controller;

import lombok.extern.slf4j.Slf4j;
import net.example.client.websocket.adapter.PaymentServiceFacadeAdapter;
import net.example.client.websocket.model.CreatePaymentWsMessage;
import net.example.client.websocket.model.PaymentStatusWsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


@Controller
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentServiceFacadeAdapter paymentServiceFacadeAdapter;

    @MessageMapping("/payments")
    @SendToUser("/queue/payments")
    PaymentStatusWsMessage createPayment(CreatePaymentWsMessage message, @Header("simpSessionId") String sessionId) {
        return paymentServiceFacadeAdapter.processPayment(message, sessionId);
    }
}
