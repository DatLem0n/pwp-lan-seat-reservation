package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.LocationCreationDto;
import com.fragment.seat_reservation.dto.LocationResponseDto;
import com.fragment.seat_reservation.entities.Location;
import com.fragment.seat_reservation.mapper.LocationMapper;
import com.fragment.seat_reservation.repositories.EventRepository;
import com.fragment.seat_reservation.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, EventRepository eventRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.locationMapper = locationMapper;
    }

    public void saveLocation(LocationCreationDto locationCreationDto) {
        Location location = new Location();
        location.setName(locationCreationDto.getName());
        location.setEvent(eventRepository.findEventById(locationCreationDto.getEvent()));

        locationRepository.save(location);
    }

    public void deleteLocation(Long eventId, Long locationId) {
        locationRepository.deleteByEventIdAndId(eventId, locationId);
    }

    public List<LocationResponseDto> findAllByEventId(Long id) {
        return locationMapper.toDtoList(locationRepository.findAllByEventId(id));
    }

    public LocationResponseDto findLocation(Long id) {
        return locationMapper.toDto(locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id ")));
    }

    public LocationResponseDto findByEventIdAndLocationId(Long eventId, Long locationId) {
        return locationMapper.toDto(locationRepository.findByEventIdAndId(eventId, locationId));
    }
}
