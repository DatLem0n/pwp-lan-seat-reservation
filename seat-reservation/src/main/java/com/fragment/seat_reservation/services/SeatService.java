package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.SeatCreationDto;
import com.fragment.seat_reservation.dto.SeatResponseDto;
import com.fragment.seat_reservation.entities.Location;
import com.fragment.seat_reservation.entities.Seat;
import com.fragment.seat_reservation.exceptions.ResourceNotFoundException;
import com.fragment.seat_reservation.mapper.SeatMapper;
import com.fragment.seat_reservation.repositories.LocationRepository;
import com.fragment.seat_reservation.repositories.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final LocationRepository locationRepository;
    private final SeatMapper seatMapper;

    public SeatService(SeatRepository seatRepository, LocationRepository locationRepository, SeatMapper seatMapper) {
        this.seatRepository = seatRepository;
        this.locationRepository = locationRepository;
        this.seatMapper = seatMapper;
    }

    public void createSeats(SeatCreationDto seatCreationDto) {
        Location location = locationRepository.findById(seatCreationDto.getLocation())
                .orElseThrow(() -> new ResourceNotFoundException("Location Not Found!"));
        var lastSeat = seatRepository.findTopByLocationIdOrderBySeatNumberDesc(seatCreationDto.getLocation());

        int lastSeatNumber = 0;

        if (lastSeat != null) {
            lastSeatNumber = lastSeat.getSeatNumber();
        }

        for (int i = 0; i < seatCreationDto.getSeatCount(); i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(++lastSeatNumber);
            seat.setType(seatCreationDto.getType());
            seat.setLocation(location);

            seatRepository.save(seat);
        }
    }

    public List<SeatResponseDto> findAllByLocationId(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Location Not Found!");
        }
        List<Seat> seats = seatRepository.findAllByLocationId(locationId);
        return seatMapper.toDtoList(seats);
    }

    @Transactional
    public void deleteSeat(DeletionDto deletionDto) {
        Long id = deletionDto.getId();
        if (!seatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seat Not Found!");
        }
        seatRepository.deleteById(id);
    }
}
