package com.example.oyster.service;

import com.example.oyster.api.request.UserRequest;
import com.example.oyster.api.response.UserResponse;
import com.example.oyster.model.Card;
import com.example.oyster.model.User;
import com.example.oyster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CardService cardService;

    @Autowired
    public UserService(UserRepository userRepository, CardService cardService) {
        this.userRepository = userRepository;
        this.cardService = cardService;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserResponse addUser(UserRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        user.setCard(cardService.linkUserCard(user, BigDecimal.ZERO));
        User userSaved = userRepository.save(user);

        UserResponse userResponse = UserResponse.builder()
                .id(userSaved.getId())
                .firstName(userSaved.getFirstName())
                .lastName(userSaved.getLastName())
                .cardId(userSaved.getCard().getId())
                .balance(userSaved.getCard().getBalance())
                .build();

        return userResponse;
    }

    public BigDecimal getBalance(String id) throws ValidationException {
        User user = findUser(Long.valueOf(id));
        return cardService.getBalance(user.getCard());
    }

    public UserResponse load(String id, BigDecimal money) throws ValidationException {
        User user = findUser(Long.valueOf(id));

        Card card = cardService.load(user.getCard(), money);
        user.setCard(card);

        User userSaved = userRepository.save(user);

        UserResponse userResponse = UserResponse.builder()
                .id(userSaved.getId())
                .firstName(userSaved.getFirstName())
                .lastName(userSaved.getLastName())
                .cardId(userSaved.getCard().getId())
                .balance(userSaved.getCard().getBalance())
                .build();

        return userResponse;
    }

    User findUser(Long userId) throws ValidationException {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ValidationException("User id not found"));
    }
}
