package com.example.oyster.service;

import com.example.oyster.api.request.EndTripRequest;
import com.example.oyster.api.request.StartTripRequest;
import com.example.oyster.exception.JourneyException;
import com.example.oyster.model.User;
import com.example.oyster.model.UserJourney;
import com.example.oyster.repository.UserJourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.time.Clock;
import java.time.ZonedDateTime;

@Service
public class UserJourneyService {
    private final UserJourneyRepository userJourneyRepository;
    private final UserService userService;
    private final Clock clock;

    @Autowired
    public UserJourneyService(UserJourneyRepository userJourneyRepository,
                              UserService userService,
                              Clock clock) {
        this.userJourneyRepository = userJourneyRepository;
        this.userService = userService;
        this.clock = clock;
    }

    @Transactional
    public UserJourney startTrip(StartTripRequest request) throws ValidationException {
        User user = userService.findUser(Long.valueOf(request.getUserId()));

        UserJourney userJourney = new UserJourney();
        ZonedDateTime now = ZonedDateTime.now(clock);

        userJourney.setStartPoint(request.getTransport(), request.getStartPoint(), user.getCard(), now);

        return userJourneyRepository.save(userJourney);
    }

    @Transactional
    public UserJourney endTrip(EndTripRequest request) throws ValidationException {
        UserJourney journey = userJourneyRepository
                .findById(request.getUserJourneyId().longValue())
                .orElseThrow(() -> new JourneyException("Invalid Journey"));

        if (journey.getToStation() != null) {
            throw new ValidationException("Trip already completed !");
        }

        journey.setEndPoint(request.getEndPoint());

        return userJourneyRepository.save(journey);
    }

}
