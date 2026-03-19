package com.fragment.seat_reservation.entities;

import jakarta.persistence.*;
import lombok.Setter;

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

    @Setter
    @Column(length = 64, nullable = false)
    private String name;

    @Setter
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Seat> seats;

    // still needs getters/setters, equals() and hashcode()
}
