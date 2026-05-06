package com.fragment.seat_reservation.api;

import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReservationControllerTests extends ControllerTestsBase {

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
    public void reservationTest() throws Exception {
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
        boolean isReserved = JsonPath.read(tmp, "$[0].reserved");
        Assertions.assertThat(isReserved).isEqualTo(false);
        Integer firstId = JsonPath.read(tmp, "$[0].id");

        String seatUrl = locationUrl + "/seats/" + firstId.toString() + "/reservation";
        mockMvc.perform(post(seatUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isCreated());

        getResult = mockMvc.perform(get(locationUrl + "/seats")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        tmp = getResult.getResponse().getContentAsString();
        isReserved = JsonPath.read(tmp, "$[0].reserved");
        Assertions.assertThat(isReserved).isEqualTo(true);

        mockMvc.perform(delete(seatUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNoContent());

        getResult = mockMvc.perform(get(locationUrl + "/seats")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        tmp = getResult.getResponse().getContentAsString();
        isReserved = JsonPath.read(tmp, "$[0].reserved");
        Assertions.assertThat(isReserved).isEqualTo(false);
    }

    @Test
    public void reserveReservedSeatTest() throws Exception {
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
        String seatUrl = locationUrl + "/seats/" + firstId.toString() + "/reservation";

        mockMvc.perform(post(seatUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isCreated());

        mockMvc.perform(post(seatUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void cancelFreeSeatTest() throws Exception {
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
        String seatUrl = locationUrl + "/seats/" + firstId.toString() + "/reservation";

        mockMvc.perform(delete(seatUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void reserveNullSeatTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        String locationName = "Test location 1";
        String locationUrl = createLocation(locationName, eventUrl);

        String seatUrl = locationUrl + "/seats/-1/reservation";

        mockMvc.perform(post(seatUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void cancelNullSeatTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        String locationName = "Test location 1";
        String locationUrl = createLocation(locationName, eventUrl);

        String seatUrl = locationUrl + "/seats/-1/reservation";

        mockMvc.perform(delete(seatUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }
}
