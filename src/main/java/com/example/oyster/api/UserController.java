package com.example.oyster.api;

import com.example.oyster.api.request.UserRequest;
import com.example.oyster.api.response.UserResponse;
import com.example.oyster.model.User;
import com.example.oyster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> fetchUsers() {
        return userService.getUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addUser(@RequestBody UserRequest request) {
        return userService.addUser(request);
    }

    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable String id) throws ValidationException {
        return userService.getBalance(id);
    }

    @PatchMapping("/{id}/topUp")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse topUp(@PathVariable String id, @RequestParam BigDecimal money) throws ValidationException {
        return userService.load(id, money);
    }
}
