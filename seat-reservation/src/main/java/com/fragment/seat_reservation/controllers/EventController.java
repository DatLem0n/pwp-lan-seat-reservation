package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.EventDto;
import com.fragment.seat_reservation.services.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/event")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDto eventDto) {
        eventService.saveEvent(eventDto);
        return ResponseEntity.status(201).body("Event created!");
    }

    @DeleteMapping("/event")
    public ResponseEntity<?> event(@Valid @RequestBody DeletionDto request) {
        eventService.deleteEvent(request);
        return ResponseEntity.status(201).body("Event removed");
    }

}
// Event endpoints
