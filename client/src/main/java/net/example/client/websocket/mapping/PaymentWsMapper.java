package net.example.client.websocket.mapping;

import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;
import net.example.client.jpa.entity.PaymentStatus;
import net.example.client.websocket.model.CreatePaymentWsMessage;
import net.example.client.websocket.model.PaymentStatusWsMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PaymentWsMapper {

    CreatePaymentTo mapCreatePaymentTo(CreatePaymentWsMessage message, String userId);

    @Mappings({
            @Mapping(target = "firstName", source = "payment.firstName"),
            @Mapping(target = "lastName", source = "payment.lastName"),
            @Mapping(target = "cardNumber", source = "payment.cardNumber"),
            @Mapping(target = "amount", source = "payment.amount"),
            @Mapping(target = "id", source = "payment.id")
    })
    PaymentStatusWsMessage mapPaymentStatusWsMessage(CreatePaymentResultTo result);

    PaymentStatusWsMessage mapPaymentStatusWsMessage(PaymentStatus status);
}
