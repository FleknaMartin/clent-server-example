package net.example.server.jpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Entity
@Table
public class Payment extends AuditableEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name ="card_id")
    private Card card;

    @Column
    private ZonedDateTime paymentTime;
}
