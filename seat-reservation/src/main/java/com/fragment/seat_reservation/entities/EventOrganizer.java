package com.fragment.seat_reservation.entities;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "user_id"}))
public class EventOrganizer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne private Event event;
    @ManyToOne private User user;

    // still needs getters/setters, equals() and hashcode()
}
