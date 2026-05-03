package com.fragment.seat_reservation.mapper;

import com.fragment.seat_reservation.dto.ReservationDto;
import com.fragment.seat_reservation.entities.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(target = "reservedFor"
            , expression = "java(seat.getReservedFor() != null ? seat.getReservedFor().getUsername() : null)")
    ReservationDto toDto(Seat seat);
    List<ReservationDto> toDtoList(List<Seat> seats);
}
