package net.example.client.jpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.example.client.api.enums.CreatePaymentResult;
import net.example.client.jpa.listener.PaymentStatusEntityListener;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;


@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Entity
@EntityListeners(PaymentStatusEntityListener.class)
public class PaymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String userId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String cardNumber;

    @Column
    private BigDecimal amount;

    @Column
    private ZonedDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    private CreatePaymentResult result;

    @Column
    private String message;
}
