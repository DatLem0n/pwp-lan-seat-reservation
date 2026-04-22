package com.fragment.seat_reservation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeatResponseDto {
    private Long id;
    private Integer seatNumber;
    private String type;
    private Boolean reserved;

}
