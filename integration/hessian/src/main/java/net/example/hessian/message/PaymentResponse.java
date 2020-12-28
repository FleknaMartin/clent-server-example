package net.example.hessian.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PaymentResponse implements Serializable {

    private static final long serialVersionUID = -8045402859523629417L;

    private Result result;
    private String message;
}
