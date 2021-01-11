package net.example.client.core.service;

import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.example.client.api.adapter.IPaymentServiceConnectorAdapter;
import net.example.client.api.enums.CreatePaymentResult;
import net.example.client.api.service.IPaymentService;
import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;
import net.example.client.jpa.entity.PaymentStatus;
import net.example.client.jpa.mapping.PaymentStatusMapper;
import net.example.client.jpa.repository.PaymentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.*;
import java.util.List;

@Service
@EnableAsync
@Slf4j
public class PaymentService implements IPaymentService {

    @Autowired
    private IPaymentServiceConnectorAdapter paymentServiceConnectorAdapter;

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Autowired
    private PaymentStatusMapper mapper;

    @Override
    public CreatePaymentResultTo processSinglePayment(CreatePaymentTo request) {
        return paymentServiceConnectorAdapter.send(request);
    }

    public PaymentStatus persistPayment(CreatePaymentTo payment, CreatePaymentResultTo result) {
        return paymentStatusRepository.save(mapper.mapPaymentStatus(payment, result));
    }

    @Override
    public List<CreatePaymentTo> loadPaymentsInStatus(List<CreatePaymentResult> results) {
        List<PaymentStatus> statuses = paymentStatusRepository.loadByStatuses(results);
        return mapper.mapCreatePaymentToList(statuses);
    }

    @Override
    @Async
    @Transactional
    public void reprocessSinglePaymentAsync(CreatePaymentTo payment) {
        PaymentStatus status = paymentStatusRepository.getOne(payment.getId());
        try {
            CreatePaymentResultTo result = paymentServiceConnectorAdapter.send(payment);
            status.setResult(result.getResult());
            status.setMessage(result.getMessage());
            paymentStatusRepository.save(status);
        } catch (StatusRuntimeException e) {
            log.error("reprocessing failed", e);
            status.setResult(CreatePaymentResult.PENDING);
            status.setMessage(e.getMessage());
            paymentStatusRepository.save(status);
        } catch (Exception e) {
            log.error("reprocessing failed", e);
            status.setResult(CreatePaymentResult.ERROR);
            status.setMessage(e.getMessage());
            paymentStatusRepository.save(status);
        }
    }
}
