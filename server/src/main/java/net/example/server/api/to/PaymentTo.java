package net.example.server.api.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTo {

    private Long id;
    private BigDecimal amount;
    private ZonedDateTime paymentTime;
    private CardTo card;
}
