package net.example.client.jpa.repository;

import net.example.client.api.enums.CreatePaymentResult;
import net.example.client.jpa.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {

    @Query("select p from PaymentStatus p where p.result in :results")
    List<PaymentStatus> loadByStatuses(@Param("results")List<CreatePaymentResult> results);
}
