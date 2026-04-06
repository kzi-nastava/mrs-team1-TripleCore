package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Passenger passenger;
    private Driver driver;

    @BeforeEach
    void setUp() {
        passenger = createPassenger();
        driver = createDriver();
    }

    private Passenger createPassenger() {
        Passenger p = new Passenger();
        p.setEmail("passenger@example.com");
        p.setPassword("password");
        p.setFirstName("Passenger");
        p.setLastName("Test");
        p.setAddress("Test Address");
        p.setPhone("123456789");
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now());
        p.setRole(UserRole.PASSENGER);
        return entityManager.persistAndFlush(p);
    }

    private Driver createDriver() {
        Driver d = new Driver();
        d.setEmail("driver@example.com");
        d.setPassword("password");
        d.setFirstName("Driver");
        d.setLastName("Test");
        d.setAddress("Test Address");
        d.setPhone("987654321");
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

    private User createUser(String email, String name, UserRole role) {
        if (role == UserRole.DRIVER) {
            Driver d = new Driver();
            d.setEmail(email);
            d.setFirstName(name);
            d.setLastName("Test");
            d.setPassword("password");
            d.setAddress("Test Address");
            d.setPhone("111111111");
            d.setRole(role);
            d.setAccountActivated(true);
            d.setAccountBlocked(false);
            d.setCreatedAt(LocalDateTime.now());
            d.setCurrentlyWorking(false);
            d.setWorkingHoursToday(0);
            d.setAvailable(true);
            d.setLastWorkStart(null);
            d.setVehicle(null);
            return entityManager.persistAndFlush(d);
        } else if (role == UserRole.PASSENGER) {
            Passenger p = new Passenger();
            p.setEmail(email);
            p.setFirstName(name);
            p.setLastName("Test");
            p.setPassword("password");
            p.setAddress("Test Address");
            p.setPhone("222222222");
            p.setRole(role);
            p.setAccountActivated(true);
            p.setAccountBlocked(false);
            p.setCreatedAt(LocalDateTime.now());
            return entityManager.persistAndFlush(p);
        } else {
            throw new IllegalArgumentException("Unknown role: " + role);
        }
    }


    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        Optional<User> found = userRepository.findByEmail(passenger.getEmail());
        assertTrue(found.isPresent());
        assertEquals(passenger.getEmail(), found.get().getEmail());
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        Optional<User> found = userRepository.findByEmail("noexisting@example.com");
        assertTrue(found.isEmpty());
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        assertTrue(userRepository.existsByEmail(driver.getEmail()));
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailDoesNotExist() {
        assertFalse(userRepository.existsByEmail("noexisting@example.com"));
    }

    @Test
    void findByRoleNot_shouldReturnOnlyUsersExcludingGivenRole() {
        createUser("driver1@example.com", "Driver 1", UserRole.DRIVER);
        User passenger2 = (User) createUser("passenger2@example.com", "Passenger 2", UserRole.PASSENGER);

        List<User> users = userRepository.findByRoleNot(UserRole.DRIVER);

        assertTrue(users.stream().allMatch(u -> u.getRole() != UserRole.DRIVER));
        assertEquals(13, users.size());
    }
}