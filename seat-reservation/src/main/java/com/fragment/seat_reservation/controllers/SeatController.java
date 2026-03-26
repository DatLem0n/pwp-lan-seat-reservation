package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.SeatCreationDto;
import com.fragment.seat_reservation.services.SeatCreationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class SeatController {
    private final SeatCreationService seatCreationService;

    public SeatController(SeatCreationService seatCreationService) {
        this.seatCreationService = seatCreationService;
    }

    @PostMapping("/seat")
    public ResponseEntity<?> seat(@Valid @RequestBody SeatCreationDto request) {
        seatCreationService.createSeats(request);
        return ResponseEntity.status(201).body("all good");
    }
}
