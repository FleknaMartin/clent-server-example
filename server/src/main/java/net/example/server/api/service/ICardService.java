package net.example.server.api.service;

import net.example.server.api.to.CardTo;

public interface ICardService {
    CardTo findCardWithCardHolderByCardNumber(CardTo card);
}
