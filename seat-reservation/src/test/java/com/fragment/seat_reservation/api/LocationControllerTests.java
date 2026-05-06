package com.fragment.seat_reservation.api;

import com.fragment.seat_reservation.dto.LocationCreationDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LocationControllerTests extends ControllerTestsBase {

    @BeforeEach
    void setup() {
        String username = "test-user-1";
        String email = "tester@testmail.com";
        createTestUser(username, email, true);
    }

    @AfterEach
    void cleanup() {
        deleteTestUser();
    }

    @Test
    void locationCreationTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        String locationName = "Test location 1";
        LocationCreationDto locCreationDto = new LocationCreationDto();
        locCreationDto.setName(locationName);

        MvcResult postResult = mockMvc.perform(post(eventUrl + "/locations")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locCreationDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String locationUrl = postResult.getResponse().getHeader("Location");
        Assertions.assertThat(locationUrl).isNotNull();

        mockMvc.perform(get(locationUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(locationName));
    }

    @Test
    public void locationDeletionTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        String locationName = "Test location 1";
        String locationUrl = createLocation(locationName, eventUrl);

        mockMvc.perform(get(locationUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(locationName));

        mockMvc.perform(delete(locationUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(locationUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void multipleLocationCreationTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        String locationName1 = "Test location 1";
        createLocation(locationName1, eventUrl);
        String locationName2 = "Test location 2";
        createLocation(locationName2, eventUrl);

        mockMvc.perform(get(eventUrl + "/locations")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void nullLocationDeletionTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        mockMvc.perform(delete(eventUrl + "/locations/-1")
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void locationCreationToNullEventTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        mockMvc.perform(delete(eventUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNoContent());

        String locationName = "Test location 1";
        LocationCreationDto locCreationDto = new LocationCreationDto();
        locCreationDto.setName(locationName);
        mockMvc.perform(post(eventUrl + "/locations")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locCreationDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void locationDeletionFromNullEventTest() throws Exception {
        mockMvc.perform(delete("/events/-1/locations/1")
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getLocationsFromNullEventTest() throws Exception {
        mockMvc.perform(get("/events/-1/locations")
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }


}
