package com.fragment.seat_reservation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationCreationDto {

    @NotEmpty(message = "Name is required")
    @Size(max = 64, message = "Name must not exceed 64 characters")
    private String name;

    private Long event;
}
