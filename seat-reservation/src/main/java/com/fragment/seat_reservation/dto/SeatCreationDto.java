package com.fragment.seat_reservation.dto;

import jakarta.validation.constraints.NotEmpty;
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
    @Size(max = 64)
    private String type;

    @Positive
    private Integer seatCount;

    private Long location;

}
