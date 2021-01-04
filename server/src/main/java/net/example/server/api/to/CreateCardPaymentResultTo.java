package net.example.server.api.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.example.server.api.enm.CreateCardTransactionResult;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardPaymentResultTo {
    private PaymentTo payment;
    private CreateCardTransactionResult result;
    private String message;
}
