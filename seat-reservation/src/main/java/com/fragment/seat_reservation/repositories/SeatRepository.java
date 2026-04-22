package com.fragment.seat_reservation.repositories;

import com.fragment.seat_reservation.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<List<Seat>> findAllByLocationId(Long locationId);
    Seat findTopByLocationIdOrderBySeatNumberDesc (Long id);
}
