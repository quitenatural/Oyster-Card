package com.example.oyster.converter;

import com.example.oyster.model.StationZone;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StationZoneConverter implements AttributeConverter<StationZone, String> {
    @Override
    public String convertToDatabaseColumn(StationZone stationZone) {
        if (stationZone != null) {
            return stationZone.name();
        }
        return null;
    }

    @Override
    public StationZone convertToEntityAttribute(String dbData) {
        if (dbData != null) {
            return StationZone.valueOf(dbData);
        }
        return null;
    }
}
