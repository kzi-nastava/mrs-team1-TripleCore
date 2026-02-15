package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideStopRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // for integration testing - loads all beans
@AutoConfigureMockMvc   // MockMvc for simulating HTTP requests
@ActiveProfiles("test") // use h2 in-memory database for testing
@Transactional  // rollback transactions after each test
public class RideStopIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private ActiveVehicleRepository activeVehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RouteRepository routeRepository;

    private Ride testRide;
    private Driver testDriver;
    private Vehicle testVehicle;
    private ActiveVehicle testActiveVehicle;
    private Route testRoute;
    private RideStopRequest testRequest;
    private Passenger testPassenger;

    @BeforeEach
    void setUp() {
        testPassenger = createPassenger();
        userRepository.save(testPassenger);

        testDriver = createDriver();
        userRepository.save(testDriver);

        testVehicle = createVehicle();
        vehicleRepository.save(testVehicle);

        testDriver.setVehicle(testVehicle);
        driverRepository.save(testDriver);

        testRoute = createRoute();
        routeRepository.save(testRoute);

        testActiveVehicle = createActiveVehicle();
        testActiveVehicle.setVehicle(testVehicle);
        activeVehicleRepository.save(testActiveVehicle);

        testRide = createRide();
        testRide.setDriver(testDriver);
        testRide.setOrderer(testPassenger);
        testRide.setRoute(testRoute);
        rideRepository.save(testRide);

        testActiveVehicle.setRide(testRide);
        activeVehicleRepository.save(testActiveVehicle);

        testRequest = createRideStopRequest();
    }

    @Test
    public void stopRideTest_whenValidRequest_thenReturnsOkWithRideStopResponse() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/rides/{id}/stop", testRide.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ride #" + testRide.getId() + " stopped successfully at " + testRequest.getAddress()))
                .andExpect(jsonPath("$.newTotalPrice").value(450.0))
                .andExpect(jsonPath("$.newDistance").value(9.0))
                .andExpect(jsonPath("$.finalAddress").value(testRequest.getAddress()));

        // Check that the ride was updated in the database
        Ride updatedRide = rideRepository.findById(testRide.getId()).orElseThrow();
        assertEquals(RideStatus.FINISHED, updatedRide.getStatus());
        assertEquals(450.0, updatedRide.getPrice());
        assertEquals(testRequest.getAddress(), updatedRide.getActualEndLocation().getAddress());
    }

    @Test
    public void stopRideTest_whenRideNotFound_thenReturnsBadRequest() throws Exception {
        // Arrange
        Long nonExistentId = 999L;

        // Act & Assert
        mockMvc.perform(post("/api/rides/{id}/stop", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void stopRideTest_whenRideNotInProgress_thenReturnsBadRequest() throws Exception {
        // Arrange - ride is not in progress (already finished)
        Ride finishedRide = createRide();
        finishedRide.setStatus(RideStatus.FINISHED);
        finishedRide.setDriver(testDriver);
        finishedRide.setOrderer(testPassenger);
        finishedRide.setRoute(testRoute);
        rideRepository.save(finishedRide);

        // Act & Assert
        mockMvc.perform(post("/api/rides/{id}/stop", finishedRide.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("not in progress")));
    }

    @Test
    public void stopRideTest_whenInvalidRequest_thenReturnsBadRequest() throws Exception {
        // Arrange - address is missing in the request
        RideStopRequest invalidRequest = new RideStopRequest();
        invalidRequest.setLatitude(45.2553);
        invalidRequest.setLongitude(19.8452);

        // Act & Assert
        mockMvc.perform(post("/api/rides/{id}/stop", testRide.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void stopRideTest_whenRideIsStopped_thenDriverAndVehicleAreUpdated() throws Exception {
        // Act
        mockMvc.perform(post("/api/rides/{id}/stop", testRide.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isOk());

        // Check that the driver was updated in the database
        Driver updatedDriver = driverRepository.findById(testDriver.getId()).orElseThrow();
        assertTrue(updatedDriver.isAvailable());
        assertFalse(updatedDriver.isCurrentlyWorking());

        // Check that the active vehicle was updated in the database
        ActiveVehicle updatedActiveVehicle = activeVehicleRepository.findById(testVehicle.getId()).orElseThrow();
        assertNull(updatedActiveVehicle.getRide());
        assertTrue(updatedActiveVehicle.isAvailable());
    }

    // helper methods to create test data

    private Passenger createPassenger() {
        Passenger p = new Passenger();
        p.setEmail("passenger@example.com");
        p.setPassword("password");
        p.setFirstName("Pera");
        p.setLastName("Peric");
        p.setAddress("Bulevar oslobođenja 1, Novi Sad");
        p.setPhone("123456789");
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now());
        p.setRole(UserRole.PASSENGER);
        return p;
    }

    private Driver createDriver() {
        Driver d = new Driver();
        d.setEmail("driver@example.com");
        d.setPassword("password");
        d.setFirstName("Mika");
        d.setLastName("Mikic");
        d.setAddress("Address");
        d.setPhone("123");
        d.setAccountActivated(true);
        d.setAccountBlocked(false);
        d.setCreatedAt(LocalDateTime.now());
        d.setRole(UserRole.DRIVER);
        d.setCurrentlyWorking(true);
        d.setWorkingHoursToday(4.5);
        d.setAvailable(false);
        d.setLastWorkStart(LocalDateTime.now().minusHours(4));
        d.setVehicle(null);
        return d;
    }

    private Vehicle createVehicle() {
        Vehicle v = new Vehicle();
        v.setBrand("Tesla");
        v.setModel("Model 3");
        v.setPlateNumber("NS123456");
        v.setSeatNumber(4);
        v.setBabyFriendly(true);
        v.setPetFriendly(false);
        v.setType(VehicleType.STANDARD);
        return v;
    }

    private Route createRoute() {
        Route r = new Route();

        Location start = new Location();
        start.setLatitude(45.2671);
        start.setLongitude(19.8335);
        start.setAddress("Bulevar oslobođenja 1, Novi Sad");
        r.setStartLocation(start);

        Location end = new Location();
        end.setLatitude(45.2553);
        end.setLongitude(19.8452);
        end.setAddress("Trg slobode 1, Novi Sad");
        r.setEndLocation(end);

        r.setEstimatedDistanceMeters(10000.0);
        r.setEstimatedDurationSeconds(900L); // 15 min

        return r;
    }

    private ActiveVehicle createActiveVehicle() {
        ActiveVehicle av = new ActiveVehicle();

        Location currentLocation = new Location();
        currentLocation.setLatitude(45.2600);
        currentLocation.setLongitude(19.8400);
        currentLocation.setAddress("Uspenska 5, Novi Sad");
        av.setLocation(currentLocation);

        av.setAvailable(false);
        av.setRouteCoordinates("[[45.2671,19.8335],[45.2600,19.8400],[45.2553,19.8452]]");
        av.setRouteIndex(1);
        return av;
    }

    private Ride createRide() {
        Ride r = new Ride();
        r.setStartTime(LocalDateTime.now().minusMinutes(10));
        r.setPrice(500.0);
        r.setBabyFriendly(false);
        r.setPetFriendly(false);
        r.setStatus(RideStatus.IN_PROGRESS);
        r.setPanic(false);
        r.setInconsistencies(null);
        return r;
    }

    private RideStopRequest createRideStopRequest() {
        RideStopRequest r = new RideStopRequest();
        r.setLatitude(45.2553);
        r.setLongitude(19.8452);
        r.setAddress("Trg slobode 1, Novi Sad");
        return r;
    }
}