package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.EventDto;
import com.fragment.seat_reservation.services.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping()
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDto eventDto) {
        eventService.saveEvent(eventDto);
        return ResponseEntity.status(201).body("Event created!");
    }

    @DeleteMapping()
    public ResponseEntity<?> event(@Valid @RequestBody DeletionDto request) {
        eventService.deleteEvent(request);
        return ResponseEntity.status(201).body("Event removed");
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<EventDto> events = eventService.findAll()
                .stream()
                .map(e -> new EventDto(e.getId(), e.getName(), e.getDescription(), e.getDate()))
                .toList();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        EventDto event = eventService.findEvent(id);
        return ResponseEntity.ok(event);
    }

}
// Event endpoints
