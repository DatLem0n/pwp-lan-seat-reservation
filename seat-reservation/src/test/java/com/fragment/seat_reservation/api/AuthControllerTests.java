package com.fragment.seat_reservation.api;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.EventDto;
import com.fragment.seat_reservation.dto.LoginRequestDto;
import com.fragment.seat_reservation.dto.UserRegistrationDto;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

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
                .andExpect(status().isCreated());
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
        registrationDto = getUserRegistrationDto(username, email, password);

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

    @Test
    public void deleteUserTest() throws Exception {
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

        MvcResult postResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        String content = postResult.getResponse().getContentAsString();
        String token = JsonPath.read(content, "$.token");
        this.bearerToken = "Bearer " + token;
        Long userId = getUserId();

        DeletionDto deletionDto = new DeletionDto();
        deletionDto.setId(userId);

        mockMvc.perform(delete("/users/" + userId)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/" + userId)
                        .header("Authorization", bearerToken))
                .andExpect(status().isUnauthorized());

        username = "test-user-2";
        email = "tester2@testmail.com";
        createTestUser(username, email, false);

        mockMvc.perform(get("/users/" + userId)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createEventWithoutPermissionTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, false);

        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        EventDto eventDto = new EventDto();
        eventDto.setName(eventName);
        eventDto.setDescription(eventDesc);
        eventDto.setDate(LocalDate.parse(eventDate));

        mockMvc.perform(post("/events")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isForbidden());
    }
}
