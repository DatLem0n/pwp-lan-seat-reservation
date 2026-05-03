package com.fragment.seat_reservation.controllers;


import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.UserProfileDto;
import com.fragment.seat_reservation.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserProfileDto> getAllUsers(Authentication authentication) {
        String username = authentication.getName();
        return userService.getAllUsers(username);
    }

    @GetMapping(path = "/{userId}")
    public UserProfileDto getUserProfile(@PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserProfile(userId, username);
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<?> changeUserData(@RequestBody UserProfileDto request,
                                            @PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        userService.changeUserData(request, userId, username);
        return ResponseEntity.status(201).body("Changes were successful");
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<?> delete(@PathVariable Long userId, Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUser(userId, username);
        return ResponseEntity.noContent().build();
    }
}
