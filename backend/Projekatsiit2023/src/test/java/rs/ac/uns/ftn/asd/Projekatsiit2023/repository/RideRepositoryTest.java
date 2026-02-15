package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RideRepositoryTest {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Passenger passenger;
    private Route route;
    private Driver driver;

    @BeforeEach
    void setUp() {
        passenger = createPassenger();
        route = createRoute();
        driver = createDriver();
    }

    // helper methods for creating entities

    private Passenger createPassenger() {
        Passenger p = new Passenger();
        p.setEmail("test@example.com");
        p.setPassword("password");
        p.setFirstName("firstName");
        p.setLastName("lastName");
        p.setAddress("Address");
        p.setPhone("123456789");
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now());
        p.setRole(UserRole.PASSENGER);
        return entityManager.persistAndFlush(p);
    }

    private Driver createDriver() {
        Driver d = new Driver();
        d.setEmail("testd@example.com");
        d.setPassword("password");
        d.setFirstName("firstName");
        d.setLastName("lastName");
        d.setAddress("Address");
        d.setPhone("123");
        d.setAccountActivated(true);
        d.setAccountBlocked(false);
        d.setCreatedAt(LocalDateTime.now());
        d.setRole(UserRole.DRIVER);
        d.setCurrentlyWorking(false);
        d.setWorkingHoursToday(0);
        d.setAvailable(true);
        d.setLastWorkStart(null);
        d.setVehicle(null);
        return entityManager.persistAndFlush(d);
    }

    private Route createRoute() {
        Route r = new Route();
        Location start = new Location();
        start.setLatitude(45.0);
        start.setLongitude(19.0);
        start.setAddress("Start Address");
        r.setStartLocation(start);

        Location end = new Location();
        end.setLatitude(45.1);
        end.setLongitude(19.1);
        end.setAddress("End Address");
        r.setEndLocation(end);

        return entityManager.persistAndFlush(r);
    }

    private Ride createRide() {
        Ride ride = new Ride();
        ride.setOrderer(passenger);
        ride.setDriver(driver);
        ride.setRoute(route);
        ride.setStartTime(LocalDateTime.now().plusHours(1));
        ride.setStatus(RideStatus.ACCEPTED);
        return rideRepository.saveAndFlush(ride);
    }

    // Tests

    @Test
    void findByPassengerId_whenPassengerHasRides_thenReturnAllRides() {
        Ride ride1 = createRide();
        Ride ride2 = createRide();

        List<Ride> rides = rideRepository.findByPassengerId(passenger.getId());

        assertEquals(2, rides.size());
    }

    @Test
    void findByPassengerId_whenPassengerHasNoRides_thenReturnEmptyList() {
        List<Ride> rides = rideRepository.findByPassengerId(999L);
        assertTrue(rides.isEmpty());
    }

    @Test
    void existsByDriverAndStatus_whenRideExists_thenReturnTrue() {
        createRide();
        assertTrue(rideRepository.existsByDriverAndStatus(driver, RideStatus.ACCEPTED));
    }

    @Test
    void existsByDriverAndStatus_whenRideDoesNotExist_thenReturnFalse() {
        assertFalse(rideRepository.existsByDriverAndStatus(driver, RideStatus.ACCEPTED));
    }

    @Test
    void existsByDriverIdAndStatus_whenRideExists_thenReturnTrue() {
        createRide();
        assertTrue(rideRepository.existsByDriverIdAndStatus(driver.getId(), RideStatus.ACCEPTED));
    }

    @Test
    void existsByDriverIdAndStatus_whenRideDoesNotExist_thenReturnFalse() {
        assertFalse(rideRepository.existsByDriverIdAndStatus(driver.getId(), RideStatus.ACCEPTED));
    }

    @Test
    void findByDriverId_whenDriverHasRides_thenReturnAllDriverRides(){
        Ride ride1 = createRide();
        Ride ride2 = createRide();

        List<Ride> rides = rideRepository.findByDriverId(driver.getId());

        assertEquals(2, rides.size());
    }

    @Test
    void findByDriverId_whenDriverHasNoRides_thenReturnEmptyList(){
        List<Ride> rides = rideRepository.findByDriverId(999L);
        assertTrue(rides.isEmpty());
    }
}

