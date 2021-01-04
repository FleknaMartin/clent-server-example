package net.example.server.api.service;

import net.example.server.api.to.CardHolderTo;

public interface ICardHolderService {

    CardHolderTo findCardHolder(CardHolderTo cardHolderTo);
}
