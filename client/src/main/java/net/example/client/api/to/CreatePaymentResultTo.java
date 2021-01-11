package net.example.client.api.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.example.client.api.enums.CreatePaymentResult;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentResultTo {
    private CreatePaymentTo payment;
    private CreatePaymentResult result;
    private String message;
}
