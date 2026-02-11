package com.fragment.seat_reservation.entities;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;


@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 256)
    private String description;

    private LocalDate date;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Location> locations;

    // still needs getters/setters, equals() and hashcode()
}
