package com.fragment.seat_reservation.controllers;


import com.fragment.seat_reservation.dto.UserProfileDto;
import com.fragment.seat_reservation.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
