package com.fragment.seat_reservation.mapper;

import com.fragment.seat_reservation.dto.EventDto;
import com.fragment.seat_reservation.entities.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDto toDto(Event event);
}
