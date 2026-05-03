package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.ReservationDto;
import com.fragment.seat_reservation.dto.SeatCreationDto;
import com.fragment.seat_reservation.dto.SeatResponseDto;
import com.fragment.seat_reservation.services.ReservationService;
import com.fragment.seat_reservation.services.SeatService;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(path = "/events/*/locations/{locationId}/seats")
public class SeatController {
    private final SeatService seatService;
    private final ReservationService reservationService;

    public SeatController(SeatService seatService, ReservationService reservationService) {
        this.seatService = seatService;
        this.reservationService = reservationService;
    }

    @GetMapping()
    public ResponseEntity<List<SeatResponseDto>> getSeats(@PathVariable Long locationId) {
        List<SeatResponseDto> seats = seatService.findAllByLocationId(locationId);
        return ResponseEntity.ok(seats);
    }

    @PostMapping()
    public ResponseEntity<?> createSeats(@Valid @RequestBody SeatCreationDto request,
                                  @PathVariable Long locationId) {
        request.setLocation(locationId);
        seatService.createSeats(request);
        Integer seatCount = request.getSeatCount();
        return ResponseEntity.status(201).body("Created " + seatCount.toString() + " seats");
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteSeat(@Valid @RequestBody DeletionDto request, @PathVariable Long locationId) {
        seatService.deleteSeat(request);
        Long id = request.getId();
        return ResponseEntity.status(200).body("Successfully deleted seat ID: " + id.toString());
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDto>> getReservations(@PathVariable Long locationId) {
        return ResponseEntity.ok(reservationService.getAllReservations(locationId));
    }
}
