package com.fragment.seat_reservation.api;

import com.fragment.seat_reservation.dto.DeletionDto;
import com.fragment.seat_reservation.dto.SeatCreationDto;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
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
public class SeatControllerTests extends ControllerTestsBase {

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
    void seatCreationTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        String locationName = "Test location 1";
        String locationUrl = createLocation(locationName, eventUrl);

        int seatNumber = 2;
        String seatType = "TEST";
        SeatCreationDto seatCreationDto = new SeatCreationDto();
        seatCreationDto.setSeatCount(seatNumber);
        seatCreationDto.setType(seatType);

        mockMvc.perform(post(locationUrl + "/seats")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seatCreationDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(locationUrl + "/seats")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(seatNumber));
    }

    @Test
    void seatDeletionTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        String locationName = "Test location 1";
        String locationUrl = createLocation(locationName, eventUrl);

        int seatNumber = 2;
        createSeats(seatNumber, locationUrl);

        MvcResult getResult = mockMvc.perform(get(locationUrl + "/seats")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(seatNumber))
                .andReturn();

        String tmp = getResult.getResponse().getContentAsString();
        Integer firstId = JsonPath.read(tmp, "$[0].id");
        Long id = firstId.longValue();

        DeletionDto deletionDto = new DeletionDto();
        deletionDto.setId(id);
        mockMvc.perform(delete(locationUrl + "/seats")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deletionDto)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(locationUrl + "/seats")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(seatNumber - 1));
    }


    @Test
    void seatCreationToNullLocationTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        int seatNumber = 2;
        String seatType = "TEST";
        SeatCreationDto seatCreationDto = new SeatCreationDto();
        seatCreationDto.setSeatCount(seatNumber);
        seatCreationDto.setType(seatType);

        mockMvc.perform(post(eventUrl + "/locations/-1/seats")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seatCreationDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void seatCreationTwiceTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        String locationName = "Test location 1";
        String locationUrl = createLocation(locationName, eventUrl);

        int seatNumber = 2;
        createSeats(seatNumber, locationUrl);

        mockMvc.perform(get(locationUrl + "/seats")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(seatNumber));

        createSeats(seatNumber, locationUrl);

        mockMvc.perform(get(locationUrl + "/seats")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(seatNumber*2));
    }

    @Test
    void getSeatsFromNullLocationTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        mockMvc.perform(get(eventUrl + "/locations/-1/seats")
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNullSeat() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        String locationName = "Test location 1";
        String locationUrl = createLocation(locationName, eventUrl);

        int seatNumber = 2;
        createSeats(seatNumber, locationUrl);

        mockMvc.perform(get(locationUrl + "/seats")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(seatNumber));

        DeletionDto deletionDto = new DeletionDto();
        deletionDto.setId(-1L);
        mockMvc.perform(delete(locationUrl + "/seats")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deletionDto)))
                .andExpect(status().isNotFound());
    }
}