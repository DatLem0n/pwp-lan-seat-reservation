package com.fragment.seat_reservation.dto;

import com.fragment.seat_reservation.entities.Event;
import com.fragment.seat_reservation.entities.Seat;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private Event event;
    private Set<Seat> seats;
}
