package com.example.oyster.model;

import com.example.oyster.exception.FareException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Table(name = "cards")
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "card")
    @JsonIgnore
    private User user;

    private BigDecimal balance;

    public Card(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void addMoney(BigDecimal money) {
        this.balance = balance.add(money);
    }

    public void validate(BigDecimal fare) throws FareException {
        if (balance.compareTo(fare) < 0)
            throw new FareException("Insufficient balance");
    }

    public void checkIn(BigDecimal fare) {
        validate(fare);
        this.balance = this.balance.subtract(fare);
    }

    public void checkOut(BigDecimal fare) {
        this.balance = this.balance.add(fare);
    }
}
