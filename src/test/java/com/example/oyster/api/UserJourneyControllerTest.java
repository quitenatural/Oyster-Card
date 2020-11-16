package com.example.oyster.api;

import com.example.oyster.api.request.EndTripRequest;
import com.example.oyster.api.request.StartTripRequest;
import com.example.oyster.exception.JourneyException;
import com.example.oyster.model.UserJourney;
import com.example.oyster.service.UserJourneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserJourneyControllerTest {
    @Mock
    UserJourneyService userJourneyService;

    UserJourneyController userJourneyController;

    @BeforeEach
    void setUp() {
        userJourneyController = new UserJourneyController(userJourneyService);
    }

    @Test
    void shouldStartTripSuccessfully() throws ValidationException {
        StartTripRequest request = mock(StartTripRequest.class);
        UserJourney userJourney = mock(UserJourney.class);

        when(userJourneyService.startTrip(request)).thenReturn(userJourney);

        UserJourney actualResponse = userJourneyController.startTrip(request);

        assertEquals(userJourney, actualResponse);
    }

    @Test
    void shouldThrowExceptionIfCardNotFound() throws ValidationException {
        StartTripRequest request = mock(StartTripRequest.class);

        doThrow(new ValidationException("Card not found")).when(userJourneyService).startTrip(request);

        ValidationException validationException = assertThrows(ValidationException.class, () -> userJourneyController.startTrip(request));
        assertEquals(validationException.getMessage(), "Card not found");
    }

    @Test
    void shouldEndTripSuccessfully() throws ValidationException {
        EndTripRequest request = mock(EndTripRequest.class);
        UserJourney userJourney = mock(UserJourney.class);

        when(userJourneyService.endTrip(request)).thenReturn(userJourney);

        UserJourney actualResponse = userJourneyController.endTrip(request);

        assertEquals(userJourney, actualResponse);
    }

    @Test
    void shouldThrowExceptionIfInvalidJourney() throws ValidationException {
        EndTripRequest request = mock(EndTripRequest.class);

        doThrow(new JourneyException("Invalid Journey")).when(userJourneyService).endTrip(request);

        JourneyException journeyException = assertThrows(JourneyException.class, () -> userJourneyController.endTrip(request));
        assertEquals(journeyException.getMessage(), "Invalid Journey");
    }
}