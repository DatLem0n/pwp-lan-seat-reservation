package com.fragment.seat_reservation.mapper;

import com.fragment.seat_reservation.dto.SeatResponseDto;
import com.fragment.seat_reservation.entities.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    @Mapping(target = "reserved", expression = "java(seat.isReserved()")
    List<SeatResponseDto> toDtoList(List<Seat> seats);
}
