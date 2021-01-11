package net.example.client.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentWsMessage{

    private String firstName;
    private String lastName;
    private String cardNumber;
    private BigDecimal amount;
}
