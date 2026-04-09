package com.fragment.seat_reservation.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class EventDtoTests {
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
        EventDto eventDto = new EventDto();
        eventDto.setName("");
        eventDto.setDescription("Event test description");
        eventDto.setDate(LocalDate.of(2100, 10, 10));
        var violations = validator.validate(eventDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Name is required");
    }

    @Test
    void emptyDateTest() {
        EventDto eventDto = new EventDto();
        eventDto.setName("Test event");
        eventDto.setDescription("");
        var violations = validator.validate(eventDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Date is required");
    }

    @Test
    void tooLongNameTest() {
        EventDto eventDto = new EventDto();
        eventDto.setName("A".repeat(100));
        eventDto.setDescription("");
        eventDto.setDate(LocalDate.of(2100, 10, 10));
        var violations = validator.validate(eventDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Name must not exceed 64 characters");
    }

    @Test
    void tooLongDescriptionTest() {
        EventDto eventDto = new EventDto();
        eventDto.setName("Test event");
        eventDto.setDescription("A".repeat(300));
        eventDto.setDate(LocalDate.of(2100, 10, 10));
        var violations = validator.validate(eventDto);

        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Description must not exceed 256 characters");
    }



}
