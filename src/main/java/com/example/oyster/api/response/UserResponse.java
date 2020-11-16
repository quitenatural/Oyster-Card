package com.example.oyster.api.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@EqualsAndHashCode
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Long cardId;
    private BigDecimal balance;
}
