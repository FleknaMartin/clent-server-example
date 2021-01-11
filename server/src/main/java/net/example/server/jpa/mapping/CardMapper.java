package net.example.server.jpa.mapping;

import net.example.server.api.to.CardTo;
import net.example.server.jpa.entity.Card;
import net.example.server.jpa.mapping.util.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "payments", ignore = true)
    CardTo mapCardTo(Card card, @Context CycleAvoidingMappingContext context);

    Card mapCard(CardTo card, @Context CycleAvoidingMappingContext context);
}
