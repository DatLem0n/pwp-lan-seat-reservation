package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.LocationCreationDto;
import com.fragment.seat_reservation.dto.LocationResponseDto;
import com.fragment.seat_reservation.services.LocationService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping()
    public ResponseEntity<?> location(@PathVariable Long eventId, @Valid @RequestBody LocationCreationDto request) {
        request.setEvent(eventId);
        locationService.saveLocation(request);
        return ResponseEntity.status(201).body("location created");
    }

    @DeleteMapping(path = "/{locationId}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long eventId, @PathVariable Long locationId) {
        locationService.deleteLocation(eventId, locationId);
        return ResponseEntity.status(200).body("location removed");
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<LocationResponseDto>> getAllLocations(@PathVariable Long eventId) {
        List<LocationResponseDto> locations = locationService.findAllByEventId(eventId);
        return ResponseEntity.ok(locations);
    }

    @GetMapping(path = "/{locationId}", produces = "application/json")
    public ResponseEntity<LocationResponseDto> getLocation(@PathVariable Long eventId, @PathVariable Long locationId) {
        LocationResponseDto location = locationService.findByEventIdAndLocationId(eventId, locationId);
        return ResponseEntity.ok(location);
    }
}
