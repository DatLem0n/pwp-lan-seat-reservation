package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.*;
import com.fragment.seat_reservation.entities.User;
import com.fragment.seat_reservation.exceptions.AlreadyExistsException;
import com.fragment.seat_reservation.exceptions.NotResourceOwnerException;
import com.fragment.seat_reservation.exceptions.ResourceNotFoundException;
import com.fragment.seat_reservation.mapper.UserProfileMapper;
import com.fragment.seat_reservation.repositories.UserRepository;
import com.fragment.seat_reservation.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserProfileMapper userProfileMapper;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserProfileMapper userProfileMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userProfileMapper = userProfileMapper;
    }

    public void saveUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new AlreadyExistsException("Username is already taken");
        }
        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new AlreadyExistsException("Email is already in use");
        }

        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setPhone(userRegistrationDto.getPhone());
        user.setDob(userRegistrationDto.getDob());
        user.setLastLogin(null);
        user.setAdmin(true);
        user.setCreationDate(LocalDate.now());

        userRepository.save(user);
    }

    public List<UserProfileDto> getAllUsers(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        if (!user.isAdmin()) {
            throw new NotResourceOwnerException("Access Denied");
        }
        return userProfileMapper.toDtoList(userRepository.findAll());
    }

    public UserProfileDto getUserProfile(Long userId, String username) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        if (!user.getUsername().equals(username)) {
            throw new NotResourceOwnerException("Access Denied");
        }
        return userProfileMapper.toDto(user);
    }


    public void deleteUser(DeletionDto deletionDto) {
        userRepository.deleteById(deletionDto.getId());
    }

    @Transactional
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid username or password");
        }
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return new AuthResponseDto(jwtService.generateToken(loginRequestDto.getUsername()));
    }
}
// business logic for users / auth
