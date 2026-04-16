package com.fragment.seat_reservation.controllers;

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

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event removed");
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<EventDto> events = eventService.findAll()
                .stream()
                .map(event -> new EventDto(event.getId(), event.getName(), event.getDescription(), event.getDate()))
                .toList();
        return ResponseEntity.ok(events);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
        EventDto event = eventService.findEvent(id);
        return ResponseEntity.ok(event);
    }

}
// Event endpoints
