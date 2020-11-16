package com.example.oyster.model;

import com.example.oyster.exception.FareException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardTest {

    @Test
    public void shouldThrowExceptionWhenValidateBalance() {
        Card card = new Card(BigDecimal.valueOf(30));

        FareException fareException = assertThrows(FareException.class, () -> card.validate(BigDecimal.valueOf(31)));
        assertEquals(fareException.getMessage(), "Insufficient balance");
    }

    @Test
    public void shouldThrowExceptionIfInsufficientBalanceWhileCheckIn() {
        Card card = new Card(BigDecimal.valueOf(30));

        FareException fareException = assertThrows(FareException.class, () -> card.checkIn(BigDecimal.valueOf(31)));
        assertEquals(fareException.getMessage(), "Insufficient balance");
    }

    @Test
    void shouldDeductMaxBalanceSuccessfullyWhileCheckin() {
        Card card = new Card(BigDecimal.valueOf(20));

        card.checkIn(BigDecimal.valueOf(8));

        assertEquals(BigDecimal.valueOf(12), card.getBalance());
    }

    @Test
    void shouldAddActualDifferenceSuccessfullyWhileCheckout() {
        Card card = new Card(BigDecimal.valueOf(20));

        card.checkOut(BigDecimal.valueOf(8));

        assertEquals(BigDecimal.valueOf(28), card.getBalance());
    }
}