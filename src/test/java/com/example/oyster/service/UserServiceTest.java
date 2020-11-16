package com.example.oyster.service;

import com.example.oyster.api.request.UserRequest;
import com.example.oyster.api.response.UserResponse;
import com.example.oyster.model.Card;
import com.example.oyster.model.User;
import com.example.oyster.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    CardService cardService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, cardService);
    }

    @Test
    void shouldGetAllUsers() {
        User mockUser = mock(User.class);
        List<User> userList = List.of(mockUser);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> users = userService.getUsers();

        assertEquals(userList, users);
    }

    @Test
    void shouldAddNewUser() {
        UserRequest request = UserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        Card linkedCard = Card.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();

        when(cardService.linkUserCard(user, BigDecimal.ZERO)).thenReturn(linkedCard);

        User linkedUserCard = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .card(linkedCard)
                .build();

        Card savedCard = Card.builder()
                .id(2L)
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();
        User savedUser = User.builder()
                .id(1L)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .card(savedCard)
                .build();
        when(userRepository.save(linkedUserCard)).thenReturn(savedUser);

        UserResponse actualResponse = userService.addUser(request);
        UserResponse expectedResponse = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .cardId(2L)
                .balance(BigDecimal.ZERO)
                .build();

        assertEquals(expectedResponse, actualResponse);
        verify(userRepository, times(1)).save(linkedUserCard);
    }

    @Test
    void shouldThrowExceptionIfUserNotFoundWhenReturningBalance() {
        ValidationException exception = assertThrows(ValidationException.class, () -> userService.getBalance("1"));
        assertEquals(exception.getMessage(), "User id not found");
    }

    @Test
    void shouldReturnBalance() throws ValidationException {
        Card card = new Card();
        User user = User.builder()
                .card(card)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardService.getBalance(card)).thenReturn(BigDecimal.TEN);

        BigDecimal balance = userService.getBalance("1");

        assertEquals(BigDecimal.TEN, balance);
    }

    @Test
    void shouldThrowExceptionIfUserNotFoundWhenLoadingCard() {
        ValidationException exception = assertThrows(ValidationException.class, () -> userService.load("1", BigDecimal.TEN));
        assertEquals(exception.getMessage(), "User id not found");
    }

    @Test
    void shouldLoadCardWithGivenAmount() throws ValidationException {
        BigDecimal money = BigDecimal.TEN;
        Card card = Card.builder()
                .id(2L)
                .balance(money)
                .build();
        User user = User.builder()
                .id(1L)
                .card(card)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Card expectedCard = Card.builder()
                .id(2L)
                .balance(BigDecimal.valueOf(20))
                .build();

        User savedUser = User.builder()
                .id(1L)
                .card(expectedCard).build();

        when(cardService.load(user.getCard(), money)).thenReturn(expectedCard);
        when(userRepository.save(user)).thenReturn(savedUser);

        UserResponse actualResponse = userService.load("1", money);
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .cardId(2L)
                .cardId(user.getCard().getId())
                .balance(BigDecimal.valueOf(20))
                .build();

        assertEquals(userResponse, actualResponse);
        verify(userRepository, times(1)).save(user);
    }
}