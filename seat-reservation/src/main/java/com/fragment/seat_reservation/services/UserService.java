package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.AuthResponseDto;
import com.fragment.seat_reservation.dto.LoginRequestDto;
import com.fragment.seat_reservation.dto.UserRegistrationDto;
import com.fragment.seat_reservation.entities.User;
import com.fragment.seat_reservation.repositories.UserRepository;
import com.fragment.seat_reservation.security.JwtService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void saveUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setPhone(userRegistrationDto.getPhoneNumber());
        user.setDob(userRegistrationDto.getDateOfBirth());
        user.setLastLogin(null);
        user.setAdmin(false);
        user.setCreationDate(LocalDate.now());

        userRepository.save(user);
    }

    public void deleteUser(DeletionDto deletionDto) {
        userRepository.deleteById(deletionDto.getId());
    }

    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return new AuthResponseDto(jwtService.generateToken(loginRequestDto.getUsername()));
    }
}
// business logic for users / auth
