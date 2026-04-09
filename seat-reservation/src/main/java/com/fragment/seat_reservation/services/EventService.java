package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.EventDto;
import com.fragment.seat_reservation.entities.Event;
import com.fragment.seat_reservation.mapper.EventMapper;
import com.fragment.seat_reservation.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public void saveEvent(EventDto eventDto) {
        Event event = new Event();
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());

        eventRepository.save(event);
    }

    public void deleteEvent(DeletionDto deletionDto) {
        eventRepository.deleteById(deletionDto.getId());
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public EventDto findEvent(Long id) {
        return eventMapper.toDto(eventRepository.findEventById(id));
    }

}
