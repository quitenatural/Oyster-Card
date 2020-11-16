package com.example.oyster.api.request;

import com.sun.istack.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
