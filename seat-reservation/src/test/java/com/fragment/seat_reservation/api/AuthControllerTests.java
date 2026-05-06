package com.fragment.seat_reservation.api;

import com.fragment.seat_reservation.dto.LoginRequestDto;
import com.fragment.seat_reservation.dto.UserRegistrationDto;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTests extends ControllerTestsBase {

    @NonNull
    private static UserRegistrationDto getUserRegistrationDto(String username, String email, String password) {
        String firstName = "Tero";
        String lastName = "Testeri";
        String phone = "123-456789";
        LocalDate dob = LocalDate.parse("1990-05-15");

        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername(username);
        registrationDto.setEmail(email);
        registrationDto.setPassword(password);
        registrationDto.setFirstName(firstName);
        registrationDto.setLastName(lastName);
        registrationDto.setPhone(phone);
        registrationDto.setDob(dob);
        return registrationDto;
    }

    @Test
    public void registerNewUserTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        String password = "1234";
        UserRegistrationDto registrationDto = getUserRegistrationDto(username, email, password);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void registerSameUsernameTwiceTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        String password = "1234";
        UserRegistrationDto registrationDto = getUserRegistrationDto(username, email, password);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated());

        email = "tester2@testmail.com";
        registrationDto = getUserRegistrationDto(username, email, password);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerSameEmailTwiceTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        String password = "1234";
        UserRegistrationDto registrationDto = getUserRegistrationDto(username, email, password);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated());

        username = "test-user-2";
        getUserRegistrationDto(username, email, password);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        String password = "1234";
        UserRegistrationDto registrationDto = getUserRegistrationDto(username, email, password);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated());

        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void loginWrongPasswordTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        String password = "1234";
        UserRegistrationDto registrationDto = getUserRegistrationDto(username, email, password);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated());

        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUsername(username);
        loginDto.setPassword("132");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }
}
