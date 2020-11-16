package com.example.oyster.service;

import com.example.oyster.model.Card;
import com.example.oyster.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CardService {

    public BigDecimal getBalance(Card card) {
        return card.getBalance();
    }

    public Card load(Card userCard, BigDecimal money) {
        userCard.addMoney(money);
        return userCard;
    }

    public Card linkUserCard(User user, BigDecimal initialBalance) {
        Card card = Card.builder()
                .balance(initialBalance)
                .user(user)
                .build();
        return card;
    }
}
