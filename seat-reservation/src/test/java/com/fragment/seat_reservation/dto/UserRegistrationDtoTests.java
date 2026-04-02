package com.fragment.seat_reservation.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRegistrationDtoTests {

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
    void InvalidEmailTest1() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setEmail("not real email");
        userDto.setUsername("Username-1");
        userDto.setFirstName("FirstName-1");
        userDto.setLastName("LastName-1");
        userDto.setPassword("password");
        userDto.setPhoneNumber("0123456789");
        var violations = validator.validate(userDto);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Invalid email address");
    }

    @Test
    void InvalidEmailTest2() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setEmail("");
        userDto.setUsername("Username-1");
        userDto.setFirstName("FirstName-1");
        userDto.setLastName("LastName-1");
        userDto.setPassword("password");
        userDto.setPhoneNumber("0123456789");
        var violations = validator.validate(userDto);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Email should not be empty");
    }

    @Test
    void InvalidPasswordTest() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setEmail("tester@mail.com");
        userDto.setUsername("Username-1");
        userDto.setFirstName("FirstName-1");
        userDto.setLastName("LastName-1");
        userDto.setPassword("");
        userDto.setPhoneNumber("0123456789");
        var violations = validator.validate(userDto);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Password should not be empty");
    }

    @Test
    void InvalidPhoneTest() {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setEmail("tester@mail.com");
        userDto.setUsername("Username-1");
        userDto.setFirstName("FirstName-1");
        userDto.setLastName("LastName-1");
        userDto.setPassword("password");
        userDto.setPhoneNumber("");
        var violations = validator.validate(userDto);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Phone number should not be empty");
    }

}
