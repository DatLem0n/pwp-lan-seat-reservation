package com.fragment.seat_reservation.api;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

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
        this.bearerToken = "Bearer eyJhbGciOiJIUzM4NCJ9." +
                "eyJzdWIiOiJqZG9lNzgiLCJpYXQiOjE3Nzc1Mzk0MjcsImV4cCI6MTc3NzYyNTgyN30." +
                "z5-Gw4v0_6erOeWglBEQPW2knXOotDbZbop6nZRKg1j-xfmKeo5Y_5cd6zg5qp_b";

        mockMvc.perform(get("/users")
                        .header("Authorization", bearerToken))
                .andExpect(status().isForbidden());
    }

}
