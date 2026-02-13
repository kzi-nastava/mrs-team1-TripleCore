package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rs.ac.uns.ftn.asd.Projekatsiit2023.config.SecurityConfig;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideStopRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideStopResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.VehicleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.*;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RideController.class)   // tests only controller layer
@Import(SecurityConfig.class)   // to handle security configuration during tests
public class RideStopControllerTest {

    @Autowired
    private MockMvc mockMvc;  // simulates HTTP requests without starting a server

    @Autowired
    private ObjectMapper objectMapper;  // for converting Java objects to JSON and vice versa

    @MockBean  // mock the service layer
    private RideStopService rideStopService;

    @MockBean
    private RideService rideService;

    @MockBean
    private RouteService routeService;

    @MockBean
    private RideCancelService rideCancelService;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private VehicleRepository vehicleRepository;

    @MockBean
    private NotificationService notificationService;

    @Test
    public void stopRideTest_whenValidRequest_thenReturnOkWithRideStopResponse() throws Exception {
        // Arrange
        Long rideId = 1L;

        RideStopRequest request = createValidRequest();

        RideStopResponse mockResponse = new RideStopResponse(
                true,
                "Ride #" + rideId + " stopped successfully at " + request.getAddress(),
                450.0,
                9.0,
                LocalDateTime.now(),
                request.getAddress()
        );

        // Mock service layer to return our response
        when(rideStopService.stopRide(eq(rideId), any(RideStopRequest.class)))
                .thenReturn(mockResponse);

        // Act & Assert - perform POST request to stop the ride and verify response
        mockMvc.perform(post("/api/rides/{id}/stop", rideId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))     // send JSON body with the request
                .andDo(print())
                .andExpect(status().isOk())  // 200 OK
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ride #1 stopped successfully at Trg slobode 1, Novi Sad"))
                .andExpect(jsonPath("$.newTotalPrice").value(450.0))
                .andExpect(jsonPath("$.newDistance").value(9.0))
                .andExpect(jsonPath("$.finalAddress").value("Trg slobode 1, Novi Sad"));
    }

    @Test
    public void stopRideTest_whenRideNotFound_thenReturnBadRequestWithErrorMessage()
            throws Exception {
        // Arrange
        Long rideId = 999L;  // invalid id

        RideStopRequest request = createValidRequest();

        String errorMessage = "Ride with ID " + rideId + " not found";

        // Mock service layer to throw an exception when trying to stop a non-existent ride
        when(rideStopService.stopRide(eq(rideId), any(RideStopRequest.class)))
                .thenThrow(new RuntimeException(errorMessage));

        // Act & Assert
        mockMvc.perform(post("/api/rides/{id}/stop", rideId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())  // 400 Bad Request
                .andExpect(content().string(errorMessage));  // body contains the error message
    }

    @Test
    public void stopRideTest_whenRideNotInProgress_thenReturnBadRequest() throws Exception {
        // Arrange
        Long rideId = 1L;
        RideStopRequest request = createValidRequest();

        when(rideStopService.stopRide(eq(rideId), any(RideStopRequest.class)))
                .thenThrow(new RuntimeException("Ride is not in progress. Only rides in progress can be stopped."));

        // Act & Assert
        mockMvc.perform(post("/api/rides/{id}/stop", rideId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("not in progress")));
    }

    @Test
    public void stopRideTest_whenInvalidRequest_thenReturnBadRequest() throws Exception {
        // Arrange - invalid request with missing address
        RideStopRequest invalidRequest = new RideStopRequest();
        invalidRequest.setLatitude(45.2553);
        invalidRequest.setLongitude(19.8452);

        // Act & Assert
        mockMvc.perform(post("/api/rides/1/stop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    private RideStopRequest createValidRequest() {
        RideStopRequest request = new RideStopRequest();
        request.setLatitude(45.2553);
        request.setLongitude(19.8452);
        request.setAddress("Trg slobode 1, Novi Sad");
        return request;
    }
}