package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideCancelRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideCancelResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.CancelerType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RideCancelServiceTest {

    private RideCancelServiceImpl rideCancelService;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private EntityManager entityManager;

    private Passenger passenger;
    private Driver driver;
    private Route route;

    @BeforeEach
    void setUp() {
        rideCancelService = new RideCancelServiceImpl(rideRepository);

        passenger = createPassenger();
        driver = createDriver();
        route = createRoute();
    }

    // helper methods to create entities

    private Passenger createPassenger() {
        Passenger p = new Passenger();
        p.setEmail("passenger@test.com");
        p.setPassword("password");
        p.setFirstName("Test");
        p.setLastName("Passenger");
        p.setAddress("Address");
        p.setPhone("123");
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now());
        p.setRole(UserRole.PASSENGER);
        return entityManager.merge(p);
    }

    private Driver createDriver() {
        Driver d = new Driver();
        d.setEmail("driver@test.com");
        d.setPassword("password");
        d.setFirstName("Test");
        d.setLastName("Driver");
        d.setAddress("Address");
        d.setPhone("123");
        d.setAccountActivated(true);
        d.setAccountBlocked(false);
        d.setCreatedAt(LocalDateTime.now());
        d.setRole(UserRole.DRIVER);
        d.setCurrentlyWorking(false);
        d.setWorkingHoursToday(0);
        d.setAvailable(true);
        d.setVehicle(null);
        return entityManager.merge(d);
    }

    private Route createRoute() {
        Route r = new Route();
        Location start = new Location();
        start.setLatitude(45.0);
        start.setLongitude(19.0);
        start.setAddress("Start");
        r.setStartLocation(start);

        Location end = new Location();
        end.setLatitude(45.1);
        end.setLongitude(19.1);
        end.setAddress("End");
        r.setEndLocation(end);

        return entityManager.merge(r);
    }

    private Ride createRide(LocalDateTime startTime) {
        Ride ride = new Ride();
        ride.setOrderer(passenger);
        ride.setDriver(driver);
        ride.setRoute(route);
        ride.setStartTime(startTime);
        ride.setStatus(RideStatus.ACCEPTED);
        return entityManager.merge(ride);
    }

    private RideCancelRequest createRequest(CancelerType type, String reason) {
        RideCancelRequest request = new RideCancelRequest();
        request.setCancelerType(type);
        request.setReason(reason);
        return request;
    }

    // tests

    @Test
    void cancelRide_whenRideNotFound_thenThrowException() {
        RideCancelRequest request = createRequest(CancelerType.PASSENGER, "Some reason");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> rideCancelService.cancelRide(999L, request));

        assertEquals("Ride with ID 999 not found", exception.getMessage());
    }

    @Test
    void cancelRide_driverWithReason_succeeds() {
        // arrange
        Ride ride = createRide(LocalDateTime.now().plusHours(1));
        RideCancelRequest request = createRequest(CancelerType.DRIVER, "Driver unavailable");

        // act
        RideCancelResponse response = rideCancelService.cancelRide(ride.getId(), request);

        // assert that ride has status CANCELLED and cancelledBy is driver
        Ride cancelledRide = rideRepository.findById(ride.getId()).orElseThrow();
        assertEquals(RideStatus.CANCELLED, cancelledRide.getStatus());
        assertEquals(driver.getId(), cancelledRide.getCancelledBy().getId());
        assertEquals("Driver unavailable", cancelledRide.getInconsistencies());

        assertTrue(response.isSuccess());
        assertEquals(CancelerType.DRIVER, response.getCancelledBy());
        assertEquals("Driver unavailable", response.getReason());
    }

    @Test
    void cancelRide_driverWithoutReason_throwsException() {
        // arrange
        Ride ride = createRide(LocalDateTime.now().plusHours(1));
        RideCancelRequest request = createRequest(CancelerType.DRIVER, null);

        // act & assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> rideCancelService.cancelRide(ride.getId(), request));

        assertEquals("Driver must provide a cancellation reason", exception.getMessage());
        assertEquals(RideStatus.ACCEPTED,
                rideRepository.findById(ride.getId()).orElseThrow().getStatus());
    }

    @Test
    void cancelRide_passengerBefore10Minutes_succeeds() {
        // arrange
        Ride ride = createRide(LocalDateTime.now().plusMinutes(15));
        RideCancelRequest request = createRequest(CancelerType.PASSENGER, "Change of plans");

        // act
        RideCancelResponse response = rideCancelService.cancelRide(ride.getId(), request);

        // assert that ride has status CANCELLED and cancelledBy is passenger
        Ride cancelledRide = rideRepository.findById(ride.getId()).orElseThrow();
        assertEquals(RideStatus.CANCELLED, cancelledRide.getStatus());
        assertEquals(passenger.getId(), cancelledRide.getCancelledBy().getId());
        assertEquals("Change of plans", cancelledRide.getInconsistencies());

        assertTrue(response.isSuccess());
        assertEquals(CancelerType.PASSENGER, response.getCancelledBy());
        assertEquals("Change of plans", response.getReason());
    }

    @Test
    void cancelRide_passengerLessThan10Minutes_throwsException() {
        // arrange
        Ride ride = createRide(LocalDateTime.now().plusMinutes(5));
        RideCancelRequest request = createRequest(CancelerType.PASSENGER, "Too late");

        // act & assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> rideCancelService.cancelRide(ride.getId(), request));

        assertEquals("Passenger can only cancel 10 minutes before ride start", exception.getMessage());
        assertEquals(RideStatus.ACCEPTED,
                rideRepository.findById(ride.getId()).orElseThrow().getStatus());
    }
}
