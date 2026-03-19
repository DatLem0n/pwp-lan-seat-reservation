package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.ReservationDto;
import com.fragment.seat_reservation.services.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> register(@Valid @RequestBody ReservationDto request){
        reservationService.reserveSeat(request);
        return ResponseEntity.status(201).body("all good");
    }
}
// Seat reservation endpoints. Reservation logic in ReservationService.
