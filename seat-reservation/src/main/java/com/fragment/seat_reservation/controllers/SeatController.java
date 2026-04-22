package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.SeatCreationDto;
import com.fragment.seat_reservation.dto.SeatResponseDto;
import com.fragment.seat_reservation.services.SeatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(path = "/events/*/locations/{locationId}/seats")
public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping()
    public ResponseEntity<List<SeatResponseDto>> seat(@PathVariable Long locationId) {
        List<SeatResponseDto> seats = seatService.findAllByLocationId(locationId);
        return ResponseEntity.ok(seats);
    }

    @PostMapping()
    public ResponseEntity<?> seat(@Valid @RequestBody SeatCreationDto request,
                                  @PathVariable Long locationId) {
        request.setLocation(locationId);
        seatService.createSeats(request);
        Integer seatCount = request.getSeatCount();
        return ResponseEntity.status(201).body("Created " + seatCount.toString() + " seats");
    }

    @DeleteMapping()
    public ResponseEntity<?> seat(@Valid @RequestBody DeletionDto request, @PathVariable Long locationId) {
        seatService.deleteSeat(request);
        Long id = request.getId();
        return ResponseEntity.status(201).body("Successfully deleted seat ID: " + id.toString());
    }
}
