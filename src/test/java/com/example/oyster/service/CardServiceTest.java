package com.example.oyster.service;

import com.example.oyster.model.Card;
import com.example.oyster.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardService();
    }

    @Test
    void shouldReturnBalance() {
        Card card = Card.builder()
                .balance(BigDecimal.TEN)
                .build();

        BigDecimal balance = cardService.getBalance(card);

        assertEquals(BigDecimal.TEN, balance);
    }

    @Test
    void shouldLoadCardWithGivenAmount() {
        Card card = Card.builder()
                .balance(BigDecimal.TEN)
                .build();

        Card cardAfterLoading = cardService.load(card, BigDecimal.TEN);

        assertEquals(BigDecimal.valueOf(20), cardAfterLoading.getBalance());
    }

    @Test
    void shouldLinkCardWhenNewUserAdded() {
        User user = mock(User.class);
        BigDecimal startingBalance = BigDecimal.ZERO;

        Card expectedCard = Card.builder()
                .balance(startingBalance)
                .user(user)
                .build();

        Card card = cardService.linkUserCard(user, startingBalance);

        assertEquals(expectedCard, card);
    }

}