package com.fragment.seat_reservation.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
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

    @Column(length = 64, nullable = false)
    private String username;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(length = 64)
    private String firstName;

    @Column(length = 64)
    private String lastName;

    @Column(length = 64, nullable = false)
    private String email;

    @Column(length = 64)
    private String phone;

    @Column(nullable = false)
    private boolean isAdmin;

    private LocalDate dob;
    private LocalDate creationDate;
    private LocalDate lastLogin;

    // still needs getters/setters, equals() and hashcode()
}
