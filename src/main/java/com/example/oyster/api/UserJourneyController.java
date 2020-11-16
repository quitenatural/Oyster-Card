package com.example.oyster.api;

import com.example.oyster.api.request.EndTripRequest;
import com.example.oyster.api.request.StartTripRequest;
import com.example.oyster.exception.JourneyException;
import com.example.oyster.model.UserJourney;
import com.example.oyster.service.UserJourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("/api/v1/travel")
public class UserJourneyController {
    private final UserJourneyService userJourneyService;

    @Autowired
    public UserJourneyController(UserJourneyService userJourneyService) {
        this.userJourneyService = userJourneyService;
    }

    @PostMapping("/startTrip")
    @ResponseStatus(HttpStatus.OK)
    public UserJourney startTrip(@RequestBody StartTripRequest request) throws ValidationException {
        return userJourneyService.startTrip(request);
    }

    @PatchMapping("/endTrip")
    @ResponseStatus(HttpStatus.OK)
    public UserJourney endTrip(@RequestBody EndTripRequest request) throws JourneyException, ValidationException {
        return userJourneyService.endTrip(request);
    }
}
