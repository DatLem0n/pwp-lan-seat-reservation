package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.ReservationDto;
import com.fragment.seat_reservation.entities.Seat;
import com.fragment.seat_reservation.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/events/*/locations/*/seats/{seatId}")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @PostMapping("/reservation")
    public ResponseEntity<?> reserveSeat(@PathVariable Long seatId, Authentication authentication){
        String username = authentication.getName();
        reservationService.reserveSeat(seatId, username);
        return ResponseEntity.status(201).body("Seat successfully reserved");
    }


    @DeleteMapping("/reservation")
    public ResponseEntity<?> removeReservation(@PathVariable Long seatId, Authentication authentication){
        String username = authentication.getName();
        reservationService.cancelReservation(seatId, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservation")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable Long seatId) {
        return ResponseEntity.ok(reservationService.getReservation(seatId));
    }
}