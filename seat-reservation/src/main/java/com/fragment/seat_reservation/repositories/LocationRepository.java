package com.fragment.seat_reservation.repositories;

import com.fragment.seat_reservation.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByEventId(Long id);

}
// location database queries
