package com.fragment.seat_reservation.mapper;

import com.fragment.seat_reservation.dto.LocationResponseDto;
import com.fragment.seat_reservation.entities.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationResponseDto toDto(Location location);
    List<LocationResponseDto> toDtoList(List<Location> locations);
}
