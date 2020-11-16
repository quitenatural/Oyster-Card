package com.example.oyster.model;

import com.example.oyster.exception.FareException;

import java.math.BigDecimal;

public class Fare {
    public static final BigDecimal ZONE_ONE_FARE = BigDecimal.valueOf(2.50);

    public static final BigDecimal ANY_ZONE_OUTSIDE_ZONE_ONE_FARE = BigDecimal.valueOf(2.00);

    public static final BigDecimal TWO_ZONES_INC_ZONE_ONE_FARE = BigDecimal.valueOf(3.00);

    public static final BigDecimal TWO_ZONES_EXC_ZONE_ONE_FARE = BigDecimal.valueOf(2.25);

    public static final BigDecimal THREE_ZONES_FAIR = BigDecimal.valueOf(3.20);

    public static final BigDecimal BUS_FARE = BigDecimal.valueOf(1.80);

    public static final BigDecimal MAX_TUBE_FARE = BigDecimal.valueOf(3.20);

    public static void validate(Transport transport, Card card) throws FareException {
        if (transport == Transport.BUS)
            card.validate(BUS_FARE);

        if (transport == Transport.TUBE)
            card.validate(MAX_TUBE_FARE);
    }

    public static void chargeMax(Transport transport, Card card) {
        if (transport == Transport.BUS)
            card.checkIn(BUS_FARE);

        if (transport == Transport.TUBE)
            card.checkIn(MAX_TUBE_FARE);
    }

    public static void charge(UserJourney journey, Transport transport, Card card) {
        StationZone fromStation = journey.getFromStation();
        StationZone endStation = journey.getToStation();

        if (transport == Transport.TUBE) {
            int countOfZones = findZones(fromStation, endStation);
            // twoZonesExcludingZone1 validation should be before twoZonesIncludingZone1 as it eliminates zone1 presence in two zones condition
            if (withinSingleZone(countOfZones) && insideZone1(fromStation, endStation)) {
                card.checkOut(MAX_TUBE_FARE.subtract(ZONE_ONE_FARE));
            } else if (withinSingleZone(countOfZones) && outsideZone1(fromStation, endStation)) {
                card.checkOut(MAX_TUBE_FARE.subtract(ANY_ZONE_OUTSIDE_ZONE_ONE_FARE));
            } else if (withinAnyTwoZones(countOfZones) && twoZonesExcludingZone1(fromStation, endStation)) {
                card.checkOut(MAX_TUBE_FARE.subtract(TWO_ZONES_EXC_ZONE_ONE_FARE));
            } else if (withinAnyTwoZones(countOfZones) && twoZonesIncludingZone1(fromStation, endStation)) {
                card.checkOut(MAX_TUBE_FARE.subtract(TWO_ZONES_INC_ZONE_ONE_FARE));
            } else if (withinAnyThreeZones(countOfZones)) {
                card.checkOut(MAX_TUBE_FARE.subtract(THREE_ZONES_FAIR));
            }
        } else if (transport == Transport.BUS) {
            card.checkOut(BigDecimal.ZERO);
        }
    }

    private static int findZones(StationZone fromStation, StationZone endStation) {
        String[] startPointZones = fromStation.getZone().split(",");
        String[] endPointZones = endStation.getZone().split(",");
        int count = Integer.MAX_VALUE;
        for (int i = 0; i < startPointZones.length; i++) {
            for (int j = 0; j < endPointZones.length; j++) {
                int startPoint = Integer.parseInt(startPointZones[i]);
                int endPoint = Integer.parseInt(endPointZones[j]);
                int diff = Math.abs(startPoint - endPoint);
                if (diff < count) {
                    count = diff;
                }
            }
        }
        return count;
    }

    private static boolean withinSingleZone(int countOfZones) {
        if (countOfZones == 0) {
            return true;
        }
        return false;
    }

    private static boolean withinAnyTwoZones(int countOfZones) {
        if (countOfZones == 1) {
            return true;
        }
        return false;
    }

    private static boolean withinAnyThreeZones(int countOfZones) {
        if (countOfZones == 2) {
            return true;
        }
        return false;
    }

    private static boolean insideZone1(StationZone fromStation, StationZone endStation) {
        if (fromStation.getZone().contains("1") && endStation.getZone().contains("1")) {
            return true;
        }
        return false;
    }

    private static boolean outsideZone1(StationZone fromStation, StationZone endStation) {
        if (fromStation.getZone().contains("1") && endStation.getZone().contains("1")) {
            return false;
        }
        return true;
    }

    private static boolean twoZonesIncludingZone1(StationZone fromStation, StationZone endStation) {
        if ((fromStation.getZone().contains("1") && endStation.getZone().contains("2"))
                || (fromStation.getZone().contains("2") && endStation.getZone().contains("1"))) {
            return true;
        }
        return false;
    }

    private static boolean twoZonesExcludingZone1(StationZone fromStation, StationZone endStation) {
        if ((fromStation.getZone().contains("2") && endStation.getZone().contains("3"))
                || (fromStation.getZone().contains("3") && endStation.getZone().contains("2"))) {
            return true;
        }
        return false;
    }

}
