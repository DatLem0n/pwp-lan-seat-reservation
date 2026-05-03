package com.fragment.seat_reservation.repositories;

import com.fragment.seat_reservation.entities.Seat;
import com.fragment.seat_reservation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByLocationId(Long locationId);
    Seat findTopByLocationIdOrderBySeatNumberDesc (Long id);

    @Modifying
    @Query("update Seat s set s.reservedFor = null where s.reservedFor = :user")
    void clearReservationsForUser(@Param("user") User user);
}
