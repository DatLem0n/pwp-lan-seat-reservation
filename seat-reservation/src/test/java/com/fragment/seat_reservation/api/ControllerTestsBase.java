package com.fragment.seat_reservation.api;

import com.fragment.seat_reservation.dto.EventDto;
import com.fragment.seat_reservation.dto.LocationCreationDto;
import com.fragment.seat_reservation.dto.SeatCreationDto;
import com.fragment.seat_reservation.dto.UserRegistrationDto;
import com.fragment.seat_reservation.entities.User;
import com.fragment.seat_reservation.exceptions.ResourceNotFoundException;
import com.fragment.seat_reservation.repositories.UserRepository;
import com.fragment.seat_reservation.security.JwtService;
import com.fragment.seat_reservation.services.UserService;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ControllerTestsBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String bearerToken;

    protected String defaultTestFirstname = "Tero";

    protected String defaultTestLastname = "Testeri";

    protected String defaultPassword = "1234";

    protected String defaultTestPhone = "123-456789";

    protected LocalDate defaultTestDob = LocalDate.parse("1993-06-11");

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private String testUsername;

    protected void createTestUser(String username, String email, boolean isAdmin) {
        UserRegistrationDto dto = new UserRegistrationDto();
        this.testUsername = username;
        dto.setUsername(username);
        dto.setPassword(defaultPassword);
        dto.setFirstName(defaultTestFirstname);
        dto.setLastName(defaultTestLastname);
        dto.setPhone(defaultTestPhone);
        dto.setEmail(email);
        dto.setDob(defaultTestDob);

        userService.saveUser(dto);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));
        if (isAdmin) {
            user.setAdmin(true);
            userRepository.save(user);
        }
        this.bearerToken = "Bearer " + getAccessToken(user.getUsername(), user.getId());
    }

    protected void deleteTestUser() {
        userService.deleteUserByUsername(testUsername);
    }

    protected String getAccessToken(String username, Long userId) {
        return jwtService.generateToken(username, userId);
    }

    protected Long getUserId() {
        return jwtService.extractUserId(bearerToken.substring(7));
    }

    protected String createEvent(String name, String description, String date) throws Exception {
        EventDto eventDto = new EventDto();
        eventDto.setName(name);
        eventDto.setDescription(description);
        eventDto.setDate(LocalDate.parse(date));

        MvcResult postResult = mockMvc.perform(post("/events")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isCreated()).andReturn();

        String eventUrl = postResult.getResponse().getHeader("Location");
        Assertions.assertThat(eventUrl).isNotNull();
        return eventUrl;
    }

    protected String createLocation(String locationName, String eventUrl) throws Exception {
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
        return locationUrl;
    }

    protected void createSeats(int seatNumber, String locationUrl) throws Exception {
        String seatType = "TEST";
        SeatCreationDto seatCreationDto = new SeatCreationDto();
        seatCreationDto.setSeatCount(seatNumber);
        seatCreationDto.setType(seatType);

        mockMvc.perform(post(locationUrl + "/seats")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seatCreationDto)))
                .andExpect(status().isCreated());
    }
}
