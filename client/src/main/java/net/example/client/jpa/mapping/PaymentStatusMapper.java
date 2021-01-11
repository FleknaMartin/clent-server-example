package net.example.client.jpa.mapping;

import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;
import net.example.client.jpa.entity.PaymentStatus;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentStatusMapper {

    PaymentStatus mapPaymentStatus(CreatePaymentTo payment, CreatePaymentResultTo result);

    CreatePaymentTo mapCreatePaymentTo(PaymentStatus status);

    List<CreatePaymentTo> mapCreatePaymentToList(List<PaymentStatus> statuses);
}
