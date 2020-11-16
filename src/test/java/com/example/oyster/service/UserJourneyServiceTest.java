package com.example.oyster.service;

import com.example.oyster.api.request.EndTripRequest;
import com.example.oyster.api.request.StartTripRequest;
import com.example.oyster.exception.JourneyException;
import com.example.oyster.model.*;
import com.example.oyster.repository.UserJourneyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.ValidationException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserJourneyServiceTest {
    @Mock
    private UserJourneyRepository userJourneyRepository;
    @Mock
    private UserService userService;
    @Mock
    private Clock clock;
    @Captor
    private ArgumentCaptor<UserJourney> userJourneyArgumentCaptor;

    private UserJourneyService userJourneyService;

    @BeforeEach
    void setUp() {
        userJourneyService = new UserJourneyService(userJourneyRepository, userService, clock);
    }

    @Test
    void shouldThrowExceptionIfUserNotFoundWhenStartingTrip() throws ValidationException {
        StartTripRequest request = StartTripRequest.builder()
                .startPoint(StationZone.HOLBORN)
                .transport(Transport.TUBE)
                .userId("1")
                .build();

        doThrow(new ValidationException("User id not found")).when(userService).findUser(1L);

        ValidationException validationException = assertThrows(ValidationException.class, () -> userJourneyService.startTrip(request));
        assertEquals(validationException.getMessage(), "User id not found");
    }

    @Test
    void shouldStartTrip() throws ValidationException {
        StartTripRequest request = StartTripRequest.builder()
                .startPoint(StationZone.HOLBORN)
                .transport(Transport.TUBE)
                .userId("1")
                .build();

        Card card = Card.builder()
                .balance(BigDecimal.TEN)
                .build();
        User user = User.builder()
                .card(card)
                .build();

        when(userService.findUser(1L)).thenReturn(user);
        when(clock.instant()).thenReturn(Instant.parse("2020-11-15T10:14:15Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));

        userJourneyService.startTrip(request);

        verify(userJourneyRepository).save(userJourneyArgumentCaptor.capture());

        UserJourney captorValue = userJourneyArgumentCaptor.getValue();

        Card card1 = Card
                .builder()
                .balance(BigDecimal.TEN.subtract(BigDecimal.valueOf(3.20)))
                .build();

        UserJourney journey = UserJourney.builder()
                .fromStation(StationZone.HOLBORN)
                .transport(Transport.TUBE)
                .date(ZonedDateTime.parse("2020-11-15T10:14:15Z[UTC]"))
                .card(card1)
                .build();
        assertEquals(journey, captorValue);
    }

    @Test
    void shouldThrowExceptionIfJourneyIdNotFound() {
        EndTripRequest request = EndTripRequest.builder()
                .userJourneyId(1)
                .build();

        JourneyException journeyException = assertThrows(JourneyException.class, () -> userJourneyService.endTrip(request));
        assertEquals(journeyException.getMessage(), "Invalid Journey");
    }

    @Test
    void shouldThrowExceptionIfTripAlreadyCompleted() {
        EndTripRequest request = EndTripRequest.builder()
                .userJourneyId(1)
                .build();
        UserJourney userJourney = UserJourney.builder()
                .toStation(StationZone.HOLBORN)
                .build();

        when(userJourneyRepository.findById(request.getUserJourneyId().longValue())).thenReturn(Optional.of(userJourney));

        ValidationException exception = assertThrows(ValidationException.class, () -> userJourneyService.endTrip(request));
        assertEquals(exception.getMessage(), "Trip already completed !");
    }

    @Test
    void shouldEndTrip() throws ValidationException {
        EndTripRequest request = EndTripRequest.builder()
                .endPoint(StationZone.EARLS_COURT)
                .userJourneyId(1)
                .build();

        UserJourney userJourney = UserJourney.builder()
                .fromStation(StationZone.HOLBORN)
                .build();

        when(userJourneyRepository.findById(request.getUserJourneyId().longValue())).thenReturn(Optional.of(userJourney));

        userJourneyService.endTrip(request);

        verify(userJourneyRepository).save(userJourneyArgumentCaptor.capture());

        UserJourney captorValue = userJourneyArgumentCaptor.getValue();

        UserJourney journey = UserJourney.builder()
                .fromStation(StationZone.HOLBORN)
                .toStation(StationZone.EARLS_COURT)
                .build();
        assertEquals(journey, captorValue);
    }
}