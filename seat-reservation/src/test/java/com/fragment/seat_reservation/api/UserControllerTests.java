package com.fragment.seat_reservation.api;

import com.fragment.seat_reservation.dto.UserProfileDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTests extends ControllerTestsBase {

    @Test
    public void getAllUsersAdminTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, true);

        mockMvc.perform(get("/users")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        String username = "test-user-2";
        String email = "testeri@testmail.com";
        createTestUser(username, email, false);

        mockMvc.perform(get("/users")
                        .header("Authorization", bearerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void invalidJWTTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, true);
        this.bearerToken = "";

        mockMvc.perform(get("/users")
                        .header("Authorization", bearerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void invalidJWT2Test() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, true);
        this.bearerToken = "eyJhbGciOiJIUzM4NCJ9." +
                "eyJzdWIiOiJqZG9lNzgiLCJpYXQiOjE3Nzc1Mzk0MjcsImV4cCI6MTc3NzYyNTgyN30." +
                "z5-Gw4v0_6erOeWglBEQPW2knXOotDbZbop6nZRKg1j-xfmKeo5Y_5cd6zg5qp_b";

        mockMvc.perform(get("/users")
                        .header("Authorization", bearerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void expiredJWTTest() throws Exception {
        String username = "test-user-1"; // Token tied to username: test-user-1
        String email = "tester@testmail.com";
        createTestUser(username, email, true);
        this.bearerToken = "Bearer eyJhbGciOiJIUzM4NCJ9." +
                "eyJzdWIiOiJ0ZXN0LXVzZXItMSIsInVzZXJJZCI6ODkwLCJp" +
                "YXQiOjE3NzgxMzM5MDksImV4cCI6MTc3ODEzMzk2OH0." +
                "YNOmgNA74ORMmESbd6Dg2rvvWesuusj_GC5cju4aRhEECWltntRQJ9N5U5jIUJtG";

        mockMvc.perform(get("/users")
                        .header("Authorization", bearerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUserProfileTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, false);

        mockMvc.perform(get("/users/" + getUserId())
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    public void getNullUserProfileTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, false);

        mockMvc.perform(get("/users/" + (-1L))
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteNullUserProfileTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, false);

        mockMvc.perform(delete("/users/" + (-1L))
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserWrongProfileTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, false);
        Long userId = getUserId();

        username = "test-user-2";
        email = "tester2@testmail.com";
        createTestUser(username, email, false);

        mockMvc.perform(get("/users/" + userId)
                        .header("Authorization", bearerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void changeUserDataTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, false);
        Long userId = getUserId();

        mockMvc.perform(get("/users/" + userId)
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value(defaultTestFirstname))
                .andExpect(jsonPath("$.lastName").value(defaultTestLastname))
                .andExpect(jsonPath("$.phone").value(defaultTestPhone));

        String newFirstname = "Pete";
        String newLastname = "Mitchell";
        String newPhone = "000-000000";
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setFirstName(newFirstname);
        profileDto.setLastName(newLastname);
        profileDto.setPhone(newPhone);

        mockMvc.perform(put("/users/" + userId)
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(get("/users/" + userId)
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value(newFirstname))
                .andExpect(jsonPath("$.lastName").value(newLastname))
                .andExpect(jsonPath("$.phone").value(newPhone));
    }

    @Test
    public void changeNullUserProfileDataTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, false);

        UserProfileDto profileDto = new UserProfileDto();

        mockMvc.perform(put("/users/" + (-1L))
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserProfileAdminTest() throws Exception {
        String username1 = "test-user-1";
        String email1 = "tester@testmail.com";
        createTestUser(username1, email1, false);
        Long userId = getUserId();

        String username2 = "test-user-2";
        String email2 = "tester2@testmail.com";
        createTestUser(username2, email2, true);

        mockMvc.perform(get("/users/" + userId)
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username1))
                .andExpect(jsonPath("$.email").value(email1));
    }

    @Test
    public void changeUserDataEmptyTest() throws Exception {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, false);
        Long userId = getUserId();

        mockMvc.perform(get("/users/" + userId)
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value(defaultTestFirstname))
                .andExpect(jsonPath("$.lastName").value(defaultTestLastname))
                .andExpect(jsonPath("$.phone").value(defaultTestPhone));

        UserProfileDto profileDto = new UserProfileDto();

        mockMvc.perform(put("/users/" + userId)
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users/" + userId)
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value(defaultTestFirstname))
                .andExpect(jsonPath("$.lastName").value(defaultTestLastname))
                .andExpect(jsonPath("$.phone").value(defaultTestPhone));
    }

}
