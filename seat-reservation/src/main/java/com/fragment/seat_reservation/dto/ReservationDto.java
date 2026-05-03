package com.fragment.seat_reservation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationDto {
    private Long seatNumber;
    private String seatType;
    private String reservedFor;
}