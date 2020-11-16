package com.example.oyster.api.request;

import com.example.oyster.model.StationZone;
import com.sun.istack.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndTripRequest {
    @NotNull
    private Integer userJourneyId;
    @NotNull
    private StationZone endPoint;
}
