package net.example.server.jpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Entity
public class Card extends AuditableEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String cardNumber;

    @ManyToOne
    @JoinColumn(name ="card_holder_id")
    private CardHolder cardHolder;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "card")
    private List<Payment> payments;
}
