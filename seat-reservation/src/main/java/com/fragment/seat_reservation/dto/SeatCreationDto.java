package com.fragment.seat_reservation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SeatCreationDto {

    @NotEmpty(message = "Type is required")
    @Size(max = 20, message = "Type must not exceed 20 characters")
    private String type;

    @Positive(message = "Seat count must be positive")
    private Integer seatCount;

    @NotNull(message = "Location id is required")
    private Long location;

}
