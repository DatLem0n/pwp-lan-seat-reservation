package com.fragment.seat_reservation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
