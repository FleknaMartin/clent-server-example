package net.example.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.example.server.api.service.ICardHolderService;
import net.example.server.api.to.CardHolderTo;
import net.example.server.jpa.entity.CardHolder;
import net.example.server.jpa.mapping.CardHolderMapper;
import net.example.server.jpa.mapping.util.CycleAvoidingMappingContext;
import net.example.server.jpa.repository.CardHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardHolderService implements ICardHolderService {

    @Autowired
    private CardHolderRepository cardHolderRepository;

    @Autowired
    private CardHolderMapper cardHolderMapper;

    @Override
    public CardHolderTo findCardHolder(CardHolderTo cardHolderTo) {
        CardHolder cardHolder = cardHolderRepository.findByFirstAndLastName(cardHolderTo.getFirstName(),
                cardHolderTo.getLastName());
        return cardHolderMapper.mapCardHolderTo(cardHolder, new CycleAvoidingMappingContext());
    }
}
