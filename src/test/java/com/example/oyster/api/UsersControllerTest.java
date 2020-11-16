package com.example.oyster.api;

import com.example.oyster.api.request.UserRequest;
import com.example.oyster.api.response.UserResponse;
import com.example.oyster.model.User;
import com.example.oyster.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {
    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void shouldReturnUsers() {
        User mockUser = mock(User.class);
        List<User> userList = List.of(mockUser);

        when(userService.getUsers()).thenReturn(userList);

        List<User> actualResponse = userController.fetchUsers();

        assertEquals(userList, actualResponse);
    }

    @Test
    void shouldAddNewUser() {
        UserRequest userRequest = mock(UserRequest.class);
        UserResponse mockUserResponse = mock(UserResponse.class);

        when(userService.addUser(userRequest)).thenReturn(mockUserResponse);

        UserResponse userResponse = userController.addUser(userRequest);

        assertEquals(mockUserResponse, userResponse);
    }

    @Test
    void shouldThrowExceptionIfUserNotFoundWhileFetchingBalance() throws ValidationException {
        String userId = "1";

        doThrow(new ValidationException("User id not found")).when(userService).getBalance(userId);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.getBalance(userId));
        assertEquals(exception.getMessage(), "User id not found");
    }

    @Test
    void shouldReturnBalance() throws ValidationException {
        BigDecimal money = BigDecimal.valueOf(30);
        String userId = "1";

        when(userService.getBalance(userId)).thenReturn(money);

        BigDecimal balance = userController.getBalance(userId);

        assertEquals(money, balance);
    }

    @Test
    void shouldThrowExceptionIfUserNotFoundWhileLoadingCard() throws ValidationException {
        String userId = "1";
        BigDecimal money = BigDecimal.TEN;

        doThrow(new ValidationException("User id not found")).when(userService).load(userId, money);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.topUp(userId, money));
        assertEquals(exception.getMessage(), "User id not found");
    }

    @Test
    void shouldLoadCardWithGivenAmount() throws ValidationException {
        String userId = "1";
        BigDecimal money = BigDecimal.TEN;
        UserResponse userResponse = mock(UserResponse.class);

        when(userService.load(userId,money)).thenReturn(userResponse);

        UserResponse actualResponse = userController.topUp(userId, money);

        assertEquals(userResponse, actualResponse);
    }
}