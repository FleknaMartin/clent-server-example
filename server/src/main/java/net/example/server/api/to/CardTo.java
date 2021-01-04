package net.example.server.api.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardTo {

    private Long id;
    private String cardNumber;
    private CardHolderTo cardHolder;
    @ToString.Exclude
    private List<PaymentTo> payments;
}
