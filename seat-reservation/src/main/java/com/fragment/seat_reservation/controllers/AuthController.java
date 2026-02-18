package com.fragment.seat_reservation.controllers;

import com.fragment.seat_reservation.dto.UserDto;
import com.fragment.seat_reservation.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody UserDto request) {

        userService.saveUser(request);
        //AuthResponse response = authService.register(request);
        return ResponseEntity.status(201).body("all good");
    }
}
// login, registration endpoints. Auth logic in userService.
