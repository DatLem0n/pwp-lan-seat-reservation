package com.fragment.seat_reservation.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = "username"),
            @UniqueConstraint(columnNames = "email")
    }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // still needs getters/setters, equals() and hashcode()
    @Setter
    @Column(length = 64, nullable = false)
    private String username;

    @Setter
    @Column(length = 64, nullable = false)
    private String password;

    @Setter
    @Column(length = 64)
    private String firstName;

    @Setter
    @Column(length = 64)
    private String lastName;

    @Setter
    @Column(length = 64, nullable = false)
    private String email;

    @Setter
    @Column(length = 64)
    private String phone;

    @Setter
    @Column(nullable = false)
    private boolean isAdmin;

    @Setter
    private LocalDate dob;
    @Setter
    private LocalDate creationDate;
    @Setter
    private LocalDate lastLogin;

}
