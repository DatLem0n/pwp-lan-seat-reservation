package com.fragment.seat_reservation.entities;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "user_id"}))
public class EventOrganizer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne private Event eventId;
    @ManyToOne private User userId;

    // still needs getters/setters, equals() and hashcode()
}
