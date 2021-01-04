package net.example.server.jpa.mapping;

import net.example.server.api.to.CardHolderTo;
import net.example.server.jpa.mapping.util.CycleAvoidingMappingContext;
import org.mapstruct.*;
import net.example.server.jpa.entity.CardHolder;

@Mapper(componentModel = "spring",
        uses = CardMapper.class)
public interface CardHolderMapper {

    CardHolder mapCardHolder(CardHolderTo cardHolder, @Context CycleAvoidingMappingContext context);

    CardHolderTo mapCardHolderTo(CardHolder cardHolder, @Context CycleAvoidingMappingContext context);
}
