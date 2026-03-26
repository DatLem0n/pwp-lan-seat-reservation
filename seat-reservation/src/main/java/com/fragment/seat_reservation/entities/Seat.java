package com.fragment.seat_reservation.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Entity
@Table(
    name = "seats",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"location_id", "seat_number"})
    }
)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

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

    public Boolean isReserved(){
        return this.getReservedFor() == null;
    }

}
