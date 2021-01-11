package net.example.client.api.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentTo {
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String cardNumber;
    private BigDecimal amount;
    private ZonedDateTime paymentTime;
}
