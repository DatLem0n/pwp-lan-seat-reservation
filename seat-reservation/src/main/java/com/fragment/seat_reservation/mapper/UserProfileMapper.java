package com.fragment.seat_reservation.mapper;

import com.fragment.seat_reservation.dto.UserProfileDto;
import com.fragment.seat_reservation.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDto toDto(User user);
    List<UserProfileDto> toDtoList(List<User> users);
}
