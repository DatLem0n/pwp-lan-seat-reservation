package com.fragment.seat_reservation.services;

import com.fragment.seat_reservation.dto.UserDto;
import com.fragment.seat_reservation.entities.User;
import com.fragment.seat_reservation.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser (UserDto userDto) {
       User user = new User();
       user.setUsername(userDto.getUsername());
       user.setFirstName(userDto.getFirstName());
       user.setLastName(userDto.getLastName());
       user.setEmail(userDto.getEmail());
       user.setPassword(userDto.getPassword());
       user.setPhone(userDto.getPhoneNumber());

       user.setDob(java.time.LocalDate.now());
       user.setLastLogin(null);
       user.setAdmin(true);
       user.setCreationDate(java.time.LocalDate.now());

       userRepository.save(user);
    }


}
// business logic for users / auth
