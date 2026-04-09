package com.fragment.seat_reservation.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SeatCreationDtoTests {

    private static ValidatorFactory factory;
    private Validator validator;

    @BeforeAll
    static void setup() {
        factory = Validation.buildDefaultValidatorFactory();
    }

    @BeforeEach
    void getValidator() {
        validator = factory.getValidator();
    }

    @AfterAll
    static void cleanup() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void emptyTypeTest() {
        SeatCreationDto seatCreationDto = new SeatCreationDto();
        seatCreationDto.setType("");
        seatCreationDto.setSeatCount(10);
        seatCreationDto.setLocation(1L);
        var violations = validator.validate(seatCreationDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Type is required");
    }

    @Test
    void tooLongTypeTest() {
        SeatCreationDto seatCreationDto = new SeatCreationDto();
        seatCreationDto.setType("A".repeat(100));
        seatCreationDto.setSeatCount(10);
        seatCreationDto.setLocation(1L);
        var violations = validator.validate(seatCreationDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Type must not exceed 20 characters");
    }

    @Test
    void emptyLocationTest() {
        SeatCreationDto seatCreationDto = new SeatCreationDto();
        seatCreationDto.setType("VIP");
        seatCreationDto.setSeatCount(10);
        var violations = validator.validate(seatCreationDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Location id is required");
    }

    @Test
    void negativeTest() {
        SeatCreationDto seatCreationDto = new SeatCreationDto();
        seatCreationDto.setType("VIP");
        seatCreationDto.setSeatCount(-1);
        seatCreationDto.setLocation(1L);
        var violations = validator.validate(seatCreationDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Seat count must be positive");
    }
}
