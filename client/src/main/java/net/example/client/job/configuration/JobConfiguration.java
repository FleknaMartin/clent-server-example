package net.example.client.job.configuration;

import net.example.client.api.enums.CreatePaymentResult;
import net.example.client.job.ProcessStoredPaymentsJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.List;

@Configuration
public class JobConfiguration {

    @Value("${job.reprocess-payments.statuses}")
    List<CreatePaymentResult> results;

    @Value("${job.reprocess-payments.repeat-interval}")
    Long repeatInterval;

    @Bean
    public JobDetailFactoryBean jobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ProcessStoredPaymentsJob.class);
        jobDetailFactory.setName(ProcessStoredPaymentsJob.class.getName());
        jobDetailFactory.setDescription("Reprocessing of stored payments with defined result");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean trigger(JobDetail job) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(job);
        trigger.setRepeatInterval(repeatInterval);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JobKeys.PAYMENT_RESULT_LIST.name(), results);
        trigger.setJobDataMap(jobDataMap);
        return trigger;
    }
}
