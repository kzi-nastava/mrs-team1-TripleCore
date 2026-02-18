package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ActiveVehicleRepositoryTest {

    @Autowired
    private ActiveVehicleRepository activeVehicleRepository;

    @Autowired
    private TestEntityManager entityManager;

    Vehicle vehicle1;

    @BeforeEach
    void setup(){
        vehicle1 = createVehicle(1);
        Vehicle vehicle2 = createVehicle(2);
        Vehicle vehicle3 = createVehicle(3);
        Driver driver1 = createDriver(1, vehicle1);
        createDriver(2, vehicle2);
        createDriver(3, vehicle3);
        Passenger passenger = createPassenger();
        Route route = createRoute();
        Ride ride = createRide(passenger, driver1, route);

        addActiveVehicle(vehicle1, ride);
        addActiveVehicle(vehicle2, null);
        addActiveVehicle(vehicle3, null);

    }

    private Vehicle createVehicle(int num){
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(String.format("Brand %d", num));
        vehicle.setModel(String.format("Model %d", num));
        vehicle.setPlateNumber(String.format("NS-%d-TX", num));
        vehicle.setSeatNumber(4);
        vehicle.setBabyFriendly(true);
        vehicle.setPetFriendly(false);
        vehicle.setType(VehicleType.STANDARD);

        return entityManager.persistAndFlush(vehicle);
    }

    private Driver createDriver(int num, Vehicle vehicle){
        Driver driver = new Driver();
        driver.setEmail(String.format("driver%d@example.com", num));
        driver.setPassword("password");
        driver.setFirstName(String.format("Driver %d", num));
        driver.setLastName(String.format("Driver %d", num));
        driver.setAddress("Address");
        driver.setPhone("+3810000000");
        driver.setRole(UserRole.DRIVER);
        driver.setAccountActivated(true);
        driver.setAccountBlocked(false);
        driver.setCreatedAt(LocalDateTime.now());

        driver.setVehicle(vehicle);
        return entityManager.persistAndFlush(driver);
    }

    private Passenger createPassenger() {
        Passenger passenger = new Passenger();

        passenger.setEmail("passenger@example.com");
        passenger.setPassword("password");
        passenger.setFirstName("Passenger");
        passenger.setLastName("Passenger");
        passenger.setAddress("Address");
        passenger.setPhone("+3810000000");
        passenger.setRole(UserRole.PASSENGER);
        passenger.setAccountActivated(true);
        passenger.setAccountBlocked(false);
        passenger.setCreatedAt(LocalDateTime.now());
        passenger.setProfileImage(null);

        return entityManager.persistAndFlush(passenger);
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

    private Ride createRide(Passenger passenger, Driver driver, Route route) {
        Ride ride = new Ride();
        ride.setOrderer(passenger);
        ride.setDriver(driver);
        ride.setRoute(route);
        ride.setStartTime(LocalDateTime.now().plusHours(1));
        ride.setStatus(RideStatus.IN_PROGRESS);
        return entityManager.persistAndFlush(ride);
    }

    private void addActiveVehicle(Vehicle vehicle, Ride ride){
        ActiveVehicle av = new ActiveVehicle();
        av.setVehicle(vehicle);
        av.setRide(ride);
        av.setAvailable(ride != null);

        activeVehicleRepository.save(av);
    }

    @Test
    void findByRideIdIsNull_shouldFindActiveVehiclesWithoutRides(){
        List<ActiveVehicle> avs = activeVehicleRepository.findByRideIdIsNull();
        assertEquals(2, avs.size());
        for (ActiveVehicle av : avs)
            assertNull(av.getRide());
    }

    @Test
    void findByRideIdIsNotNull_shouldFindActiveVehiclesWithRides(){
        List<ActiveVehicle> avs = activeVehicleRepository.findByRideIdIsNotNull();
        assertEquals(1, avs.size());
        for (ActiveVehicle av : avs)
            assertNotNull(av.getRide());
    }

    @Test
    void findByVehicleId_shouldFindActiveVehicle_whenValidVehicleId(){
        Optional<ActiveVehicle> avOpt = activeVehicleRepository.findByVehicleId(vehicle1.getId());

        assertTrue(avOpt.isPresent());
        ActiveVehicle av = avOpt.get();
        assertEquals(av.getVehicleId(), vehicle1.getId());
    }

    @Test
    void findByVehicleId_shouldReturnEmpty_whenInvalidVehicleId(){
        Optional<ActiveVehicle> avOpt = activeVehicleRepository.findByVehicleId(999L);
        assertTrue(avOpt.isEmpty());
    }
}

