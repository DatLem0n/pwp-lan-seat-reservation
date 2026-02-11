package com.fragment.seat_reservation.entities;

import jakarta.persistence.*;

@Entity
@Table(
    name = "seats",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"location_id", "seat_number"})
    }
)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64)
    private String type;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "reserved_for_user_id")
    private User reservedFor;

    // still needs getters/setters, equals() and hashcode()
}
