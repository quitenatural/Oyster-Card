package com.example.oyster.model;

import com.example.oyster.converter.StationZoneConverter;
import com.example.oyster.exception.FareException;
import com.example.oyster.exception.JourneyException;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Table(name = "user_journey")
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserJourney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    private Transport transport;

    @Convert(converter = StationZoneConverter.class)
    private StationZone fromStation;

    @Convert(converter = StationZoneConverter.class)
    private StationZone toStation;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public void setStartPoint(Transport transport, StationZone startPoint, Card card, ZonedDateTime now) {
        try {
            Fare.validate(transport, card);
            Fare.chargeMax(transport, card);
        } catch (FareException e) {
            throw new FareException(e.getMessage());
        }
        this.transport = transport;
        this.card = card;
        this.fromStation = startPoint;
        this.date = now;
    }

    public void setEndPoint(StationZone endPoint) throws JourneyException {
        this.toStation = endPoint;
        Fare.charge(this, transport, card);
    }
}
