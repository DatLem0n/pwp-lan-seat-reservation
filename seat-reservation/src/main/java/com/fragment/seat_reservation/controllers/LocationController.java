package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.LocationCreationDto;
import com.fragment.seat_reservation.dto.LocationResponseDto;
import com.fragment.seat_reservation.entities.Location;
import com.fragment.seat_reservation.services.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/events/{eventId}/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping()
    public ResponseEntity<?> location(@PathVariable Long eventId, @Valid @RequestBody LocationCreationDto request) {
        request.setEvent(eventId);
        Location savedLocation = locationService.saveLocation(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedLocation.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/{locationId}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long eventId, @PathVariable Long locationId) {
        locationService.deleteLocation(eventId, locationId);
        return ResponseEntity.noContent().build();
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
