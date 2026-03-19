package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.EventDto;
import com.fragment.seat_reservation.services.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDto eventDto) {
        eventService.saveEvent(eventDto);
        return ResponseEntity.status(201).body("Event created!");
    }
}
// Event endpoints
