package com.example.oyster.model;

public enum Transport {
    BUS("BUS"),
    TUBE("TUBE");

    private String transport;

    Transport(String zone) {
        this.transport = zone;
    }

    public String getTransport() {
        return transport;
    }
}
