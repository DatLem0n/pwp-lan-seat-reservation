package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.EventDto;
import com.fragment.seat_reservation.entities.Event;
import com.fragment.seat_reservation.repositories.EventRepository;

public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void saveEvent(EventDto eventDto) {
        Event event = new Event();
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());

        eventRepository.save(event);
    }

}
