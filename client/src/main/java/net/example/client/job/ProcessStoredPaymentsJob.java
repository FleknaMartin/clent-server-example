package net.example.client.job;

import net.example.client.api.enums.CreatePaymentResult;
import net.example.client.api.servicefacade.IPaymentServiceFacade;
import net.example.client.job.configuration.JobKeys;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessStoredPaymentsJob implements Job {

    @Autowired
    private IPaymentServiceFacade paymentServiceFacade;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<CreatePaymentResult> results = (List<CreatePaymentResult>)jobExecutionContext.getMergedJobDataMap()
                .get(JobKeys.PAYMENT_RESULT_LIST.name());
        paymentServiceFacade.reprocessStoredPayments(results);
    }
}
