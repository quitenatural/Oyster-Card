package com.example.oyster.api.request;

import com.example.oyster.model.StationZone;
import com.example.oyster.model.Transport;
import com.sun.istack.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartTripRequest {
    @NotNull
    private String userId;
    @NotNull
    private Transport transport;
    @NotNull
    private StationZone startPoint;
}
