package net.example.server.api.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardHolderTo {

    private Long id;
    private String firstName;
    private String lastName;
    @ToString.Exclude
    private List<CardTo> cards;
}
