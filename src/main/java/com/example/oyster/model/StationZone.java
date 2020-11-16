package com.example.oyster.model;

public enum StationZone {
    HOLBORN("1"),
    EARLS_COURT("1,2"),
    WIMBLEDON("3"),
    HAMMERSMITH("2");

    private String zone;

    StationZone(String zone) {
        this.zone = zone;
    }

    public String getZone() {
        return zone;
    }

//    public static StationZone fromString(String text) {
//        StationZone.
//        return new StationZone(text);
//    }
}
