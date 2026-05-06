package com.fragment.seat_reservation.api;

import com.fragment.seat_reservation.dto.EventDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EventControllerTests extends ControllerTestsBase {

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
    public void eventCreationTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        EventDto eventDto = new EventDto();
        eventDto.setName(eventName);
        eventDto.setDescription(eventDesc);
        eventDto.setDate(LocalDate.parse(eventDate));

        MvcResult postResult = mockMvc.perform(post("/events")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String eventUrl = postResult.getResponse().getHeader("Location");
        Assertions.assertThat(eventUrl).isNotNull();

        mockMvc.perform(get(eventUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(eventName))
                .andExpect(jsonPath("$.description").value(eventDesc))
                .andExpect(jsonPath("$.date").value(eventDate));
    }

    @Test
    public void eventDeletionTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";

        String eventUrl = createEvent(eventName, eventDesc, eventDate);

        mockMvc.perform(get(eventUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(eventName))
                .andExpect(jsonPath("$.description").value(eventDesc))
                .andExpect(jsonPath("$.date").value(eventDate));

        mockMvc.perform(delete(eventUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(eventUrl)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllEventsTest() throws Exception {
        String eventName = "Test event 1";
        String eventDesc = "This is a test event";
        String eventDate = "2030-12-12";
        createEvent(eventName, eventDesc, eventDate);

        String eventName2 = "Test event 2";
        String eventDesc2 = "This is a test event 2";
        String eventDate2 = "2030-12-10";
        createEvent(eventName2, eventDesc2, eventDate2);

        mockMvc.perform(get("/events")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void nullEventDeletionTest() throws Exception {
        mockMvc.perform(delete("/events/-1")
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }
}
