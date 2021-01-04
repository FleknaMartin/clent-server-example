package net.example.server.core.service;

import lombok.extern.slf4j.Slf4j;
import net.example.server.api.service.ICardService;
import net.example.server.api.to.CardTo;
import net.example.server.jpa.entity.Card;
import net.example.server.jpa.mapping.CardMapper;
import net.example.server.jpa.mapping.util.CycleAvoidingMappingContext;
import net.example.server.jpa.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardService implements ICardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Override
    public CardTo findCardWithCardHolderByCardNumber(CardTo cardTo) {
        Card card = cardRepository.findCardWithCardHolderByCardNumber(cardTo.getCardNumber());
        return cardMapper.mapCardTo(card, new CycleAvoidingMappingContext());
    }
}
