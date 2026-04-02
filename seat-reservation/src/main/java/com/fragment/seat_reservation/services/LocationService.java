package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.LocationCreationDto;
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

    public void saveLocation(LocationCreationDto locationCreationDto) {
        Location location = new Location();
        location.setName(locationCreationDto.getName());
        location.setEvent(eventRepository.findEventById(locationCreationDto.getEvent()));

        locationRepository.save(location);
    }

    public void deleteLocation(DeletionDto deletionDto) {
        locationRepository.deleteById(deletionDto.getId());
    }

}
