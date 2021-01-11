package net.example.client.websocket.service;

import net.example.client.api.service.IPaymentStatusService;
import net.example.client.jpa.entity.PaymentStatus;
import net.example.client.websocket.mapping.PaymentWsMapper;
import net.example.client.websocket.model.PaymentStatusWsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class PaymentStatusService implements IPaymentStatusService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private PaymentWsMapper mapper;

    @Async
    public void postUpdate(PaymentStatus status) {
        PaymentStatusWsMessage message = mapper.mapPaymentStatusWsMessage(status);
        String userId = status.getUserId();
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, userId);
        messagingTemplate.convertAndSendToUser(userId, "/queue/payments", message, accessor.getMessageHeaders());
    }
}
