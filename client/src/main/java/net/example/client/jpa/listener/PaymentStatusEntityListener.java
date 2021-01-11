package net.example.client.jpa.listener;

import lombok.extern.slf4j.Slf4j;
import net.example.client.api.service.IPaymentStatusService;
import net.example.client.jpa.entity.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PostUpdate;

@Service
@Slf4j
public class PaymentStatusEntityListener {

    @Autowired(required = false)
    private IPaymentStatusService paymentStatusService;

    @PostUpdate
    public void postUpdate(PaymentStatus status) {
        if (paymentStatusService != null) {
            paymentStatusService.postUpdate(status);
        }
    }
}
