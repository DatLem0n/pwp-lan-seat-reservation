package com.fragment.seat_reservation.dto;

import com.fragment.seat_reservation.entities.Seat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationCreationDto {

    @NotEmpty(message = "Name is required")
    @Size(max = 64, message = "Name must not exceed 64 characters")
    private String name;

    @NotNull(message = "Event id is required")
    private Long event;

    private Set<Seat> seats;
}
