package net.example.server.grpc.mapping;

import net.example.grpc.PaymentRequest;
import net.example.grpc.PaymentResponse;
import net.example.server.api.to.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static net.example.server.util.TestConstants.*;

class PaymentServiceEndpointMapperTest {

    private PaymentServiceEndpointMapper mapper = Mappers.getMapper(PaymentServiceEndpointMapper.class);

    @Test
    public void createCardTransactionTo2PaymentRequest_Success() {
        CreateCardPaymentTo source = createTo();

        PaymentRequest result = mapper.mapPaymentRequest(source);

        Assertions.assertEquals(FIRST_NAME, result.getFirstName());
        Assertions.assertEquals(LAST_NAME, result.getLastName());
        Assertions.assertEquals(TIMESTAMP, result.getTimestamp());
        Assertions.assertEquals(GRPC_BIG_DECIMAL, result.getAmount());
        Assertions.assertEquals(CARD_NUMBER, result.getCardNumber());
    }

    @Test
    public void paymentRequest2CreateCardTransactionTo_Success() {
        PaymentRequest source = PaymentRequest.newBuilder()
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setCardNumber(CARD_NUMBER)
                .setAmount(GRPC_BIG_DECIMAL)
                .setTimestamp(TIMESTAMP)
                .build();

        CreateCardPaymentTo result = mapper.mapCreateCardTransactionTo(source);

        Assertions.assertEquals(FIRST_NAME, result.getPayment().getCard().getCardHolder().getFirstName());
        Assertions.assertEquals(LAST_NAME, result.getPayment().getCard().getCardHolder().getLastName());
        Assertions.assertEquals(ZONED_DATE_TIME, result.getPayment().getPaymentTime());
        Assertions.assertEquals(BIG_DECIMAL, result.getPayment().getAmount());
        Assertions.assertEquals(CARD_NUMBER, result.getPayment().getCard().getCardNumber());
    }

    @Test
    public void CreateCardTransactionResultTo2PaymentResponse_SuccessResult_Success(){
        CreateCardPaymentResultTo source = new CreateCardPaymentResultTo();
        source.setMessage(MESSAGE);
        source.setResult(RESULT_ENUM_SUCCESS);

        PaymentResponse result = mapper.mapPaymentResponse(source);

        Assertions.assertEquals(MESSAGE, result.getMessage());
        Assertions.assertEquals(GRPC_RESULT_ENUM_SUCCESS, result.getResult());
    }

    @Test
    public void CreateCardTransactionResultTo2PaymentResponse_FailureResult_Success(){
        CreateCardPaymentResultTo source = new CreateCardPaymentResultTo();
        source.setMessage(MESSAGE);
        source.setResult(RESULT_ENUM_FAILURE);

        PaymentResponse result = mapper.mapPaymentResponse(source);

        Assertions.assertEquals(MESSAGE, result.getMessage());
        Assertions.assertEquals(GRPC_RESULT_ENUM_FAILURE, result.getResult());
    }

    private CreateCardPaymentTo createTo() {
        CardHolderTo cardHolder = new CardHolderTo();
        cardHolder.setFirstName(FIRST_NAME);
        cardHolder.setLastName(LAST_NAME);

        CardTo card = new CardTo();
        card.setCardHolder(cardHolder);
        card.setCardNumber(CARD_NUMBER);

        PaymentTo payment = new PaymentTo();
        payment.setCard(card);
        payment.setAmount(BIG_DECIMAL);
        payment.setPaymentTime(ZONED_DATE_TIME);
        CreateCardPaymentTo source = new CreateCardPaymentTo();
        source.setPayment(payment);
        return source;
    }

}