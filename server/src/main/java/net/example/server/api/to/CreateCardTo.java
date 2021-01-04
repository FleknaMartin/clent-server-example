package net.example.server.api.to;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardTo {

    private CardHolderTo person;
    private CardTo card;
}
