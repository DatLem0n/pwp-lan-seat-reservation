package com.fragment.seat_reservation.dto;

import com.fragment.seat_reservation.entities.Seat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
public class LocationResponseDto {
    private Long id;
    private String name;
}
