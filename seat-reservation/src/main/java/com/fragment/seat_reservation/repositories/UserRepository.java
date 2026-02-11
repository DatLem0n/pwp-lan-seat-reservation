package com.fragment.seat_reservation.repositories;

import com.fragment.seat_reservation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
// user database queries
