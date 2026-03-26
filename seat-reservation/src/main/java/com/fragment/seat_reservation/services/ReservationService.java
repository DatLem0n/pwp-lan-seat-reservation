package com.fragment.seat_reservation.services;
import com.fragment.seat_reservation.dto.ReservationDto;
import com.fragment.seat_reservation.entities.Seat;
import com.fragment.seat_reservation.repositories.SeatRepository;
import com.fragment.seat_reservation.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    public ReservationService(SeatRepository seatRepository, UserRepository userRepository) {
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void reserveSeat(ReservationDto reservationDto) {
        Seat seat = seatRepository.findById(reservationDto.getSeatId())
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with id " + reservationDto.getSeatId()));

        var user = userRepository.findById(reservationDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + reservationDto.getUserId()));

        if (seat.isReserved()) {
            throw new IllegalStateException("Seat " + seat.getId() + " is already reserved");
        }

        seat.setReservedFor(user);
        seatRepository.save(seat);
    }
}
