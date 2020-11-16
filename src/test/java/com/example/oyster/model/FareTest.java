package com.example.oyster.model;

import com.example.oyster.exception.FareException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FareTest {

    @Test
    public void testValidateBusFareException() {
        Card card = new Card(BigDecimal.ZERO);

        FareException fareException = assertThrows(FareException.class, () -> Fare.validate(Transport.BUS, card));
        assertEquals(fareException.getMessage(), "Insufficient balance");
    }

    @Test
    public void testValidateTubeFareException() {
        Card card = new Card(BigDecimal.ZERO);

        FareException fareException = assertThrows(FareException.class, () -> Fare.validate(Transport.TUBE, card));
        assertEquals(fareException.getMessage(), "Insufficient balance");
    }

    @Test
    public void testChargeMaxBus() {
        Card card = new Card(Fare.BUS_FARE);

        Fare.chargeMax(Transport.BUS, card);
        assertEquals(BigDecimal.ZERO.setScale(1), card.getBalance());
    }


    @Test
    public void testChargeMaxTube() {
        Card card = new Card(Fare.MAX_TUBE_FARE);

        Fare.chargeMax(Transport.TUBE, card);
        assertEquals(BigDecimal.ZERO.setScale(1), card.getBalance());
    }


    @Test
    public void testChargeBus() {
        Card card = new Card(Fare.BUS_FARE);
        UserJourney userJourney = new UserJourney();
        userJourney.setStartPoint(Transport.BUS, null, card, ZonedDateTime.now());
        userJourney.setEndPoint(null);

        Fare.charge(userJourney, Transport.BUS, card);
        assertEquals(BigDecimal.ZERO.setScale(1), card.getBalance());
    }

    @Test
    public void testChargeTubeWithinZoneOne() {
        Card card = new Card(Fare.MAX_TUBE_FARE);
        UserJourney userJourney = new UserJourney();
        userJourney.setStartPoint(Transport.TUBE, StationZone.HOLBORN, card, ZonedDateTime.now());
        userJourney.setEndPoint(StationZone.EARLS_COURT);

        assertEquals(Fare.MAX_TUBE_FARE.subtract(Fare.ZONE_ONE_FARE), card.getBalance());
    }

    @Test
    public void testChargeTubeAnyZoneOutSideZoneOne() {
        Card card = new Card(Fare.MAX_TUBE_FARE);
        UserJourney userJourney = new UserJourney();
        userJourney.setStartPoint(Transport.TUBE, StationZone.HAMMERSMITH, card, ZonedDateTime.now());
        userJourney.setEndPoint(StationZone.EARLS_COURT);


        assertEquals(Fare.MAX_TUBE_FARE.subtract(Fare.ANY_ZONE_OUTSIDE_ZONE_ONE_FARE), card.getBalance());
    }

    @Test
    public void testChargeTubeAnyTwoZonesIncludingZoneOne() {
        Card card = new Card(Fare.MAX_TUBE_FARE);
        UserJourney userJourney = new UserJourney();
        userJourney.setStartPoint(Transport.TUBE, StationZone.HAMMERSMITH, card, ZonedDateTime.now());
        userJourney.setEndPoint(StationZone.HOLBORN);

        assertEquals(Fare.MAX_TUBE_FARE.subtract(Fare.TWO_ZONES_INC_ZONE_ONE_FARE), card.getBalance());
    }

    @Test
    public void testChargeTubeAnyTwoZonesExcludingZoneOne() {
        Card card = new Card(Fare.MAX_TUBE_FARE);
        UserJourney userJourney = new UserJourney();
        userJourney.setStartPoint(Transport.TUBE, StationZone.HAMMERSMITH, card, ZonedDateTime.now());
        userJourney.setEndPoint(StationZone.WIMBLEDON);

        assertEquals(Fare.MAX_TUBE_FARE.subtract(Fare.TWO_ZONES_EXC_ZONE_ONE_FARE), card.getBalance());
    }

    @Test
    public void testChargeTubeAnyThreeZones() {
        Card card = new Card(Fare.MAX_TUBE_FARE);
        UserJourney userJourney = new UserJourney();
        userJourney.setStartPoint(Transport.TUBE, StationZone.HOLBORN, card, ZonedDateTime.now());
        userJourney.setEndPoint(StationZone.WIMBLEDON);

        assertEquals(Fare.MAX_TUBE_FARE.subtract(Fare.THREE_ZONES_FAIR), card.getBalance());
    }
}