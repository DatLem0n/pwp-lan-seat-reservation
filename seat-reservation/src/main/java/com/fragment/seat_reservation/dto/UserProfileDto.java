package com.fragment.seat_reservation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserProfileDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dob;
}
