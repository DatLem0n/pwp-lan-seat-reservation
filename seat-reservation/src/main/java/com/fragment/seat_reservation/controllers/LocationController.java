package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.LocationCreationDto;
import com.fragment.seat_reservation.services.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/location")
    public ResponseEntity<?> location(@Valid @RequestBody LocationCreationDto request) {
        locationService.saveLocation(request);
        return ResponseEntity.status(201).body("location created");
    }

    @DeleteMapping("/location")
    public ResponseEntity<?> location(@Valid @RequestBody DeletionDto request) {
        locationService.deleteLocation(request);
        return ResponseEntity.status(200).body("location removed");
    }

}
