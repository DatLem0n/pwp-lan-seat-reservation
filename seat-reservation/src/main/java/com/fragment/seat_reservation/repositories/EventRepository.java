package com.fragment.seat_reservation.repositories;

import com.fragment.seat_reservation.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository <Event, Long> {
    Event findEventByName(String name);
    Event findEventById(Long id);
}
// event database queries
