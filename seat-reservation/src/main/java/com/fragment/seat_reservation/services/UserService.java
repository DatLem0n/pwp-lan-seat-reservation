package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.UserRegistrationDto;
import com.fragment.seat_reservation.entities.User;
import com.fragment.seat_reservation.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser (UserRegistrationDto userRegistrationDto) {
       User user = new User();
       user.setUsername(userRegistrationDto.getUsername());
       user.setFirstName(userRegistrationDto.getFirstName());
       user.setLastName(userRegistrationDto.getLastName());
       user.setEmail(userRegistrationDto.getEmail());
       user.setPassword(userRegistrationDto.getPassword());
       user.setPhone(userRegistrationDto.getPhoneNumber());

       user.setDob(java.time.LocalDate.now());
       user.setLastLogin(null);
       user.setAdmin(true);
       user.setCreationDate(java.time.LocalDate.now());

       userRepository.save(user);
    }


}
// business logic for users / auth
