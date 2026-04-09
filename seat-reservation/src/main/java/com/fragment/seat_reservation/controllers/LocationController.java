package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.LocationCreationDto;
import com.fragment.seat_reservation.dto.LocationResponseDto;
import com.fragment.seat_reservation.services.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping()
    public ResponseEntity<?> location(@Valid @RequestBody LocationCreationDto request) {
        locationService.saveLocation(request);
        return ResponseEntity.status(201).body("location created");
    }

    @DeleteMapping()
    public ResponseEntity<?> location(@Valid @RequestBody DeletionDto request) {
        locationService.deleteLocation(request);
        return ResponseEntity.status(200).body("location removed");
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<LocationResponseDto>> getAllLocations(@PathVariable Long eventId) {
        List<LocationResponseDto> locations = locationService.findAllByEventId(eventId);
        return ResponseEntity.ok(locations);
    }

}
