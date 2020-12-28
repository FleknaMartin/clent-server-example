package net.example.hessian.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class PaymentRequest implements Serializable {

    private static final long serialVersionUID = -9013352202378098196L;

    private String firstName;
    private String lastName;
    private String cardNumber;
    private BigDecimal amount;
    private ZonedDateTime timestamp;

}
