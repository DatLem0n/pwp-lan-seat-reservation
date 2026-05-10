package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.LocationCreationDto;
import com.fragment.seat_reservation.dto.LocationResponseDto;
import com.fragment.seat_reservation.entities.Event;
import com.fragment.seat_reservation.entities.Location;
import com.fragment.seat_reservation.exceptions.ResourceNotFoundException;
import com.fragment.seat_reservation.mapper.LocationMapper;
import com.fragment.seat_reservation.repositories.EventRepository;
import com.fragment.seat_reservation.repositories.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final LocationMapper locationMapper;
    private final UserService userService;


    public LocationService(LocationRepository locationRepository, EventRepository eventRepository,
                           LocationMapper locationMapper, UserService userService) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.locationMapper = locationMapper;
        this.userService = userService;
    }

    public Location saveLocation(LocationCreationDto locationCreationDto, String username) {
        userService.validateOrganizerPermission(username);
        Location location = new Location();
        location.setName(locationCreationDto.getName());
        Long eventId = locationCreationDto.getEvent();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event Not Found!"));
        location.setEvent(event);

        locationRepository.save(location);
        return location;
    }

    @Transactional
    public void deleteLocation(Long eventId, Long locationId, String username) {
        userService.validateOrganizerPermission(username);
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event Not Found!");
        }

        Location location = locationRepository.findByEventIdAndId(eventId, locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location Not Found!"));
        locationRepository.delete(location);
    }

    public List<LocationResponseDto> findAllByEventId(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event Not Found!");
        }
        List<Location> locations = locationRepository.findAllByEventId(id);
        return locationMapper.toDtoList(locations);
    }

    public LocationResponseDto findByEventIdAndLocationId(Long eventId, Long locationId) {
        return locationMapper.toDto(locationRepository.findByEventIdAndId(eventId, locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location Not Found!")));
    }
}
