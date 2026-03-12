package com.fragment.seat_reservation.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;

    @NotEmpty(message = "Name is required")
    @Size(max = 64, message = "Name must not exceed 64 characters")
    private String name;

    @Size(max = 256, message = "Description must not exceed 256 characters")
    private String description;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date cannot be in the past!")
    private LocalDate date;
}
