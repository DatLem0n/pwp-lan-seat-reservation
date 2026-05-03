package com.fragment.seat_reservation.services;
import com.fragment.seat_reservation.dto.ReservationDto;
import com.fragment.seat_reservation.entities.Seat;
import com.fragment.seat_reservation.entities.User;
import com.fragment.seat_reservation.exceptions.AlreadyExistsException;
import com.fragment.seat_reservation.exceptions.ResourceNotFoundException;
import com.fragment.seat_reservation.mapper.ReservationMapper;
import com.fragment.seat_reservation.repositories.SeatRepository;
import com.fragment.seat_reservation.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ReservationMapper reservationMapper;

    public ReservationService(SeatRepository seatRepository,
                              UserRepository userRepository, UserService userService, ReservationMapper reservationMapper) {
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.reservationMapper = reservationMapper;
    }

    @Transactional
    public void reserveSeat(Long seatId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat Not Found!"));

        if (seat.isReserved()) {
            throw new AlreadyExistsException("Seat is already reserved!");
        }

        seat.setReservedFor(user);
        seatRepository.save(seat);
    }

    @Transactional
    public void cancelReservation(Long seatId, String username) {
        userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat Not Found!"));

        if (!seat.isReserved()) {
            throw new AlreadyExistsException("Seat " + seat.getId() + " is not reserved!");
        }

        userService.validatePermission(seat.getReservedFor(), username);
        seat.setReservedFor(null);
        seatRepository.save(seat);
    }

    @Transactional
    public ReservationDto getReservation(Long seatId) {
        //Seat seat = seatRepository.findById(seatId)
                //.orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        //ReservationDto reservationDto = new ReservationDto();
        //reservationDto.setSeatNumber(seat.getId());
        //reservationDto.setSeatType(seat.getType());
        //reservationDto.setReservedFor(seat.getReservedFor().getUsername());

        //return reservationDto;

        return reservationMapper.toDto(seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found")));
    }

    @Transactional
    public List<ReservationDto> getAllReservations(Long locationId) {
        return reservationMapper.toDtoList(seatRepository.findAllByLocationId(locationId));
    }
}
