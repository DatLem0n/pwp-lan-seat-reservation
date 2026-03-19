package com.fragment.seat_reservation.repositories;

import com.fragment.seat_reservation.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
