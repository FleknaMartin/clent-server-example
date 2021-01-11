package net.example.client.grpc.mapping;

import net.example.client.api.enums.CreatePaymentResult;
import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;
import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = UtilMapper.class)
public interface PaymentConnectorMapper {

    @Mappings({
            @Mapping(target = "amount", source = "amount", qualifiedByName = "bigDecimal2GrpcBigDecimal"),
            @Mapping(target = "timestamp", source = "paymentTime", qualifiedByName = "zonedDateTime2GrpcTimestamp"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "cardNumber", source = "cardNumber"),
    })
    PaymentRequest PaymentRequestMapper(CreatePaymentTo to);

    @Mappings({
            @Mapping(target = "result", source = "result"),
            @Mapping(target = "message", source = "message")
    })
    CreatePaymentResultTo paymentResponseMapper(PaymentResponse to);

    static CreatePaymentResult mapResult(PaymentResponse.Result result){
        switch(result){
            case SUCCESS:
                return CreatePaymentResult.CREATED;
            case FAILURE:
                return CreatePaymentResult.FAILED_SERVER;
            default:
                throw new IllegalArgumentException("Unknown enum value: " + result);
        }
    }
}
