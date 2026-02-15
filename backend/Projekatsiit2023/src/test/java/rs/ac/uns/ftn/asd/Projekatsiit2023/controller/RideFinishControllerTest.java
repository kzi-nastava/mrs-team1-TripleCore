package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.NotificationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static rs.ac.uns.ftn.asd.Projekatsiit2023.test_data.TestDataFactory.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // telling the system to use the test database
@Transactional
public class RideFinishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ActiveVehicleRepository activeVehicleRepository;

    @Autowired
    private NotificationService notificationService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public NotificationService notificationService() {
            // Making mock notification service to stop mail sending
            // That is not the service being tested
            return org.mockito.Mockito.mock(NotificationService.class);
        }
    }

    @Test
    void finishRide_shouldReturn200_whenRideInProgress() throws Exception {

        Vehicle vehicle = vehicleRepository.save(createVehicle());
        Driver driver = driverRepository.save(createDriver(vehicle));
        Passenger passenger = passengerRepository.save(createPassenger());
        Route route = routeRepository.save(createRoute());
        Ride ride = rideRepository.save(createRideInProgress(driver, passenger, route));
        ActiveVehicle av = activeVehicleRepository.save(createActiveVehicle(driver.getVehicle(), ride));

        mockMvc.perform(post("/api/rides/{id}/finish", ride.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Ride finished successfully"));

        Ride updatedRide = rideRepository.findById(ride.getId()).orElseThrow();
        assertThat(updatedRide.getStatus()).isEqualTo(RideStatus.FINISHED);

        ActiveVehicle updatedAv = activeVehicleRepository.findById(av.getVehicleId()).orElseThrow();
        assertThat(updatedAv.getRide()).isNull();
    }

    @Test
    void finishRide_shouldReturn404_whenRideDoesNotExist() throws Exception {

        mockMvc.perform(post("/api/rides/{id}/finish", 9999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void finishRide_shouldReturn404_whenActiveVehicleDoesNotExist() throws Exception {

        Vehicle vehicle = vehicleRepository.save(createVehicle());
        Driver driver = driverRepository.save(createDriver(vehicle));
        Passenger passenger = passengerRepository.save(createPassenger());
        Route route = routeRepository.save(createRoute());
        Ride ride = rideRepository.save(createRideInProgress(driver, passenger, route));

        mockMvc.perform(post("/api/rides/{id}/finish", ride.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void finishRide_shouldReturn400_whenRideIsNotInProgress() throws Exception {
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        Driver driver = driverRepository.save(createDriver(vehicle));
        Passenger passenger = passengerRepository.save(createPassenger());
        Route route = routeRepository.save(createRoute());
        Ride ride = rideRepository.save(createRequestedRide(driver, passenger, route));
        ActiveVehicle av = activeVehicleRepository.save(createActiveVehicle(driver.getVehicle(), ride));

        mockMvc.perform(post("/api/rides/{id}/finish", ride.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void finishRide_shouldReturn400_whenActiveVehicleIsNotLinkedToRide() throws Exception{
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        Driver driver = driverRepository.save(createDriver(vehicle));
        Passenger passenger = passengerRepository.save(createPassenger());
        Route route = routeRepository.save(createRoute());
        Ride ride = rideRepository.save(createRideInProgress(driver, passenger, route));
        ActiveVehicle av = activeVehicleRepository.save(createActiveVehicle(driver.getVehicle(), null));

        mockMvc.perform(post("/api/rides/{id}/finish", ride.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void finishRide_shouldReturn500_whenGenericException() throws Exception{
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        Driver driver = driverRepository.save(createDriver(vehicle));
        Passenger passenger = passengerRepository.save(createPassenger());
        Route route = routeRepository.save(createRoute());
        Ride ride = rideRepository.save(createRideInProgress(driver, passenger, route));
        ActiveVehicle av = activeVehicleRepository.save(createActiveVehicle(driver.getVehicle(), ride));

        doThrow(new RuntimeException("Notification failure"))
                .when(notificationService).rideFinishNotifyPassengers(ride);

        mockMvc.perform(post("/api/rides/{id}/finish", ride.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
