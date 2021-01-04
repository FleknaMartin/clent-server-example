package net.example.server.jpa.mapping;

import net.example.server.api.to.PaymentTo;
import net.example.server.jpa.entity.Payment;
import net.example.server.jpa.mapping.util.CycleAvoidingMappingContext;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentTo mapPaymentTo(Payment payment, @Context CycleAvoidingMappingContext context);

    Payment mapPayment(PaymentTo payment, @Context CycleAvoidingMappingContext context);
}
