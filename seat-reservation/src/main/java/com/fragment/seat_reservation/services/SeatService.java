package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.SeatCreationDto;
import com.fragment.seat_reservation.entities.Seat;
import com.fragment.seat_reservation.repositories.LocationRepository;
import com.fragment.seat_reservation.repositories.SeatRepository;
import org.springframework.stereotype.Service;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final LocationRepository locationRepository;

    public SeatService(SeatRepository seatRepository, LocationRepository locationRepository) {
        this.seatRepository = seatRepository;
        this.locationRepository = locationRepository;
    }

    public void createSeats(SeatCreationDto seatCreationDto) {

        var location = locationRepository.findById(seatCreationDto.getLocation())
                .orElseThrow(() -> new IllegalArgumentException("Location not found with id " + seatCreationDto.getLocation()));

        var lastSeat = seatRepository.findTopByLocationIdOrderBySeatNumberDesc(seatCreationDto.getLocation());
                //orElseThrow(() -> new IllegalArgumentException("No seats in location " + seatCreationDto.getLocation()));

        int lastSeatNumber = 0;

        if (lastSeat != null) {
            lastSeatNumber = lastSeat.getSeatNumber();
        }

        for (int i = 0; i < seatCreationDto.getSeatCount(); i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(i + lastSeatNumber + 1);
            seat.setType(seatCreationDto.getType());
            seat.setLocation(location);

            seatRepository.save(seat);
        }
    }

    public void deleteSeat(DeletionDto deletionDto) {
        seatRepository.deleteById(deletionDto.getId());
    }
}
