package com.fragment.seat_reservation.repositories;

import com.fragment.seat_reservation.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByEventId(Long id);
    Location findByEventIdAndId(Long eventId, Long id);
    void deleteByEventIdAndId(Long eventId, Long id);
}
// location database queries
