package com.fragment.seat_reservation.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocationCreationDtoTests {
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
    void emptyNameTest() {
        LocationCreationDto locationCreationDto = new LocationCreationDto();
        locationCreationDto.setName("");
        locationCreationDto.setEvent(1L);
        var violations = validator.validate(locationCreationDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Name is required");
    }

    @Test
    void tooLongNameTest() {
        LocationCreationDto locationCreationDto = new LocationCreationDto();
        locationCreationDto.setName("A".repeat(100));
        locationCreationDto.setEvent(1L);
        var violations = validator.validate(locationCreationDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Name must not exceed 64 characters");
    }

    @Test
    void emptyEventIdTest() {
        LocationCreationDto locationCreationDto = new LocationCreationDto();
        locationCreationDto.setName("Test Location");
        var violations = validator.validate(locationCreationDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Event id is required");
    }
}
