package net.example.client.core.servicefacade;

import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.example.client.api.enums.CreatePaymentResult;
import net.example.client.api.service.IPaymentService;
import net.example.client.api.servicefacade.IPaymentServiceFacade;
import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;
import net.example.client.jpa.entity.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class PaymentServiceFacade implements IPaymentServiceFacade {

    @Autowired
    private IPaymentService paymentService;

    @Override
    public CreatePaymentResultTo processPayment(CreatePaymentTo payment) {
        if (payment.getPaymentTime() == null) {
            payment.setPaymentTime(ZonedDateTime.now());
        }
        CreatePaymentResultTo result = new CreatePaymentResultTo();
        try {
            result = paymentService.processSinglePayment(payment);
            result.setPayment(payment);
            PaymentStatus status = paymentService.persistPayment(payment, result);
            result.getPayment().setId(status.getId());
            return result;
        } catch (StatusRuntimeException e) {
            log.error("Payment creation failed:", e);
            result.setResult(CreatePaymentResult.PENDING);
            result.setMessage(e.getMessage());
            PaymentStatus status = paymentService.persistPayment(payment, result);
            result.setPayment(payment);
            result.getPayment().setId(status.getId());
            return result;
        } catch (Exception e) {
            log.error("Payment creation failed with unexpected exception:", e);
            result.setResult(CreatePaymentResult.ERROR);
            result.setMessage(e.getMessage());
            PaymentStatus status = paymentService.persistPayment(payment, result);
            result.setPayment(payment);
            result.getPayment().setId(status.getId());
            return result;
        }
    }

    @Override
    public void reprocessStoredPayments(List<CreatePaymentResult> results) {
        List<CreatePaymentTo> payments = paymentService.loadPaymentsInStatus(results);
        payments.forEach(p -> paymentService.reprocessSinglePaymentAsync(p));
    }
}
