package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.LocationDto;
import com.fragment.seat_reservation.entities.Location;
import com.fragment.seat_reservation.repositories.EventRepository;
import com.fragment.seat_reservation.repositories.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;

    public LocationService(LocationRepository locationRepository, EventRepository eventRepository) { this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
    }

    public void saveLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setName(locationDto.getName());
        location.setEvent(eventRepository.findEventById(locationDto.getEvent()));

        locationRepository.save(location);
    }

}
