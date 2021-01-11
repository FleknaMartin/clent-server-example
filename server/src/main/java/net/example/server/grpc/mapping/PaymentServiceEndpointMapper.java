package net.example.server.grpc.mapping;

import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import net.example.server.api.enm.CreateCardTransactionResult;
import net.example.server.api.to.CreateCardPaymentTo;
import net.example.server.api.to.CreateCardPaymentResultTo;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = UtilMapper.class,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PaymentServiceEndpointMapper {

    @Mappings({
            @Mapping(target = "amount", source = "payment.amount", qualifiedByName = "bigDecimal2GrpcBigDecimal"),
            @Mapping(target = "timestamp", source = "payment.paymentTime", qualifiedByName = "zonedDateTime2GrpcTimestamp"),
            @Mapping(target = "firstName", source = "payment.card.cardHolder.firstName"),
            @Mapping(target = "lastName", source = "payment.card.cardHolder.lastName"),
            @Mapping(target = "cardNumber", source = "payment.card.cardNumber")
    })
    PaymentRequest mapPaymentRequest(CreateCardPaymentTo to);

    @Mappings({
            @Mapping(target = "payment.amount", source = "amount", qualifiedByName = "grpcBigDecimal2BigDecimal"),
            @Mapping(target = "payment.paymentTime", source = "timestamp", qualifiedByName = "grpcTimestamp2ZonedDateTime"),
            @Mapping(target = "payment.card.cardHolder.firstName", source = "firstName"),
            @Mapping(target = "payment.card.cardHolder.lastName", source = "lastName"),
            @Mapping(target = "payment.card.cardNumber", source = "cardNumber")
    })
    CreateCardPaymentTo mapCreateCardTransactionTo(PaymentRequest request);

    @Mappings({
            @Mapping(target = "result", source = "result"),
            @Mapping(target = "message", source = "message")
    })
    PaymentResponse mapPaymentResponse(CreateCardPaymentResultTo to);

    static PaymentResponse.Result mapResult(CreateCardTransactionResult result){
        switch(result){
            case SUCCESS:
                return PaymentResponse.Result.SUCCESS;
            case FAILURE:
                return PaymentResponse.Result.FAILURE;
            default:
                throw new IllegalArgumentException("Unknown enum value: " + result);
        }
    }
}
