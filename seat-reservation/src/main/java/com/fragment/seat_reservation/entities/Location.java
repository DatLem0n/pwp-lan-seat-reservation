package com.fragment.seat_reservation.entities;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(
        name = "locations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "name"})
        }
)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer seatCount;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Seat> seats;

    // still needs getters/setters, equals() and hashcode()
}
