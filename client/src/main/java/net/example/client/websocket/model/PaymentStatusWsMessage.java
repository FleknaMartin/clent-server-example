package net.example.client.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.example.client.api.enums.CreatePaymentResult;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusWsMessage{
    private Long id;
    private String firstName;
    private String lastName;
    private String cardNumber;
    private BigDecimal amount;
    private CreatePaymentResult result;
    private String message;
}
