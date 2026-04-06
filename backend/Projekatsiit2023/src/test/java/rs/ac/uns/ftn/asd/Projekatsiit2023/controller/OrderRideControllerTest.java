package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.NotificationService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.test_data.TestDataFactory;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderRideControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ActiveVehicleRepository activeVehicleRepository;

    @Autowired
    private NotificationService notificationService;

    @TestConfiguration
    static class TestConfig{
        @Bean
        public NotificationService notificationService(){
            // mockovanje da ne bi slao prave mejlove
            return org.mockito.Mockito.mock(NotificationService.class);
        }
    }


    private static String asJsonString(final Object obj){
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }



    @Test
    void orderRide_shouldReturn400_whenNoEmailHeader() throws Exception{
        RideRequest request = new RideRequest();
        request.setStartLocation(new Location(45.0, 19.0, "Lokacija 1"));
        request.setEndLocation(new Location(45.1, 19.1, "Lokacija 2"));
        request.setVehicleType(VehicleType.STANDARD);
        mockMvc.perform(post("/api/rides").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))).andExpect(status().isBadRequest());
    }


    @Test
    void orderRide_shouldReturn400_whenPassengerBlocked() throws Exception{
        Passenger passenger = passengerRepository.save(TestDataFactory.createPassenger());
        passenger.setAccountBlocked(true);
        passengerRepository.save(passenger);

        RideRequest request = new RideRequest();

        mockMvc.perform(post("/api/rides").header("X-User-Email", passenger.getEmail()).
                contentType(MediaType.APPLICATION_JSON).content(asJsonString(request))).andExpect(status().isBadRequest());

    }


    @Test
    void orderRide_shouldReturn400_whenNoDriverAvailable() throws Exception{
        Passenger passenger = passengerRepository.save(TestDataFactory.createPassenger());
        Route route = routeRepository.save(TestDataFactory.createRoute());

        RideRequest request = new RideRequest();
        request.setStartLocation(route.getStartLocation());
        request.setEndLocation(route.getEndLocation());
        request.setVehicleType(VehicleType.STANDARD);

        mockMvc.perform(post("/api/rides").header("X-User-Email", passenger.getEmail()).
                contentType(MediaType.APPLICATION_JSON).content(asJsonString(request))).andExpect(status().isBadRequest());

    }

    @Test
    void orderRide_shouldReturn400_whenScheduledTooFar() throws Exception{
        Passenger passenger = passengerRepository.save(TestDataFactory.createPassenger());
        Route route = routeRepository.save(TestDataFactory.createRoute());

        RideRequest request = new RideRequest();
        request.setStartLocation(route.getStartLocation());
        request.setEndLocation(route.getEndLocation());
        request.setVehicleType(VehicleType.STANDARD);
        request.setStartTime(LocalDateTime.now().plusHours(10));

        mockMvc.perform(post("/api/rides").header("X-User-Email", passenger.getEmail()).
                contentType(MediaType.APPLICATION_JSON).content(asJsonString(request))).andExpect(status().isBadRequest());

    }

    @Test
    void orderRide_shouldReturn200_whenRequestValid() throws Exception{
        Vehicle vehicle = vehicleRepository.save(TestDataFactory.createVehicle());

        for (VehicleType type : VehicleType.values()) {
            Price price = new Price();
            price.setVehicleType(type);
            price.setPrice(100.0);
            priceRepository.save(price);
        }

        Driver driver = driverRepository.save(TestDataFactory.createDriver(vehicle));

        ActiveVehicle activeVehicle = new ActiveVehicle();
        activeVehicle.setVehicle(vehicle);
        activeVehicle.setAvailable(true);
        activeVehicleRepository.save(activeVehicle);

        Passenger passenger = passengerRepository.save(TestDataFactory.createPassenger());
        Route route = routeRepository.save(TestDataFactory.createRoute());

        RideRequest request = new RideRequest();
        request.setStartLocation(route.getStartLocation());
        request.setEndLocation(route.getEndLocation());
        request.setVehicleType(vehicle.getType());

        doNothing().when(notificationService).notifyDriverNewRide(any());

        mockMvc.perform(post("/api/rides").header("X-User-Email", passenger.getEmail())
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void orderRide_shouldReturn500_whenGenericException() throws Exception{

        Vehicle vehicle = vehicleRepository.save(TestDataFactory.createVehicle());

        Driver driver = driverRepository.save(TestDataFactory.createDriver(vehicle));

        ActiveVehicle activeVehicle = new ActiveVehicle();
        activeVehicle.setVehicle(vehicle);
        activeVehicle.setAvailable(true);

        activeVehicleRepository.save(activeVehicle);

        Passenger passenger = passengerRepository.save(TestDataFactory.createPassenger());
        Route route = routeRepository.save(TestDataFactory.createRoute());

        for (VehicleType type : VehicleType.values()) {
            Price price = new Price();
            price.setVehicleType(type);
            price.setPrice(100.0);
            priceRepository.save(price);
        }

        RideRequest request = new RideRequest();
        request.setStartLocation(route.getStartLocation());
        request.setEndLocation(route.getEndLocation());
        request.setVehicleType(vehicle.getType());

        // simulicija greske u servisu
        doThrow(new RuntimeException("Unexpected error")).when(notificationService).notifyDriverNewRide(any());

        mockMvc.perform(post("/api/rides").header("X-User-Email", passenger.getEmail()).
                contentType(MediaType.APPLICATION_JSON).content(asJsonString((request)))).andExpect(status().isInternalServerError());


    }


}
