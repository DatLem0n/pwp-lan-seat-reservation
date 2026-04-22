package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.EventDto;
import com.fragment.seat_reservation.entities.Event;
import com.fragment.seat_reservation.exceptions.ResourceNotFoundException;
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
        this.eventMapper =  eventMapper;
    }

    public void saveEvent(EventDto eventDto) {
        Event event = new Event();
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());

        eventRepository.save(event);
    }

    public EventDto findEvent(Long id) {
        return eventMapper.toDto(eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event Not Found!")));
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event Not Found!");
        }
        eventRepository.deleteById(id);
    }

    public List<EventDto> findAll() {
        return eventMapper.toDtoList(eventRepository.findAll());
    }

}
