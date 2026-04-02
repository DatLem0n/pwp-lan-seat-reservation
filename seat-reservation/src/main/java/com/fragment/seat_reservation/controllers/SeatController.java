package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.SeatCreationDto;
import com.fragment.seat_reservation.services.SeatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping("/seat")
    public ResponseEntity<?> seat(@Valid @RequestBody SeatCreationDto request) {
        seatService.createSeats(request);
        return ResponseEntity.status(201).body("all good");
    }

    @DeleteMapping("/seat")
    public ResponseEntity<?> seat(@Valid @RequestBody DeletionDto request) {
        seatService.deleteSeat(request);
        return ResponseEntity.status(201).body("all good");
    }
}
