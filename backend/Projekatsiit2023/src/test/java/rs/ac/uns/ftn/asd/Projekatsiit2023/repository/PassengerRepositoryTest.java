package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PassengerRepositoryTest {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Passenger passenger1;
    private Passenger passenger2;

    @BeforeEach
    void setUp() {
        passenger1 = createPassenger("passenger1@example.com", "Passenger 1");
        passenger2 = createPassenger("passenger2@example.com", "Passenger 2");
    }

    private Passenger createPassenger(String email, String name) {
        Passenger p = new Passenger();
        p.setEmail(email);
        p.setPassword("password");
        p.setFirstName(name);
        p.setLastName("Test");
        p.setAddress("Test Adresa");
        p.setPhone("123456789");
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now());
        p.setRole(UserRole.PASSENGER);

        return entityManager.persistAndFlush(p);
    }


    @Test
    void findAllByEmailIn_shouldReturnAllMatchingPassengers() {
        List<String> emails = List.of(passenger1.getEmail(), passenger2.getEmail());

        List<Passenger> result = passengerRepository.findAllByEmailIn(emails);

        assertEquals(2, result.size());
    }

    @Test
    void findAllByEmailIn_shouldReturnEmptyList_whenNoEmailsMatch() {
        List<String> emails = List.of("none1@example.com", "none2@example.com");

        List<Passenger> result = passengerRepository.findAllByEmailIn(emails);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByEmailIn_shouldReturnPartialMatch() {
        List<String> emails = List.of(passenger1.getEmail(), "notfound@example.com");

        List<Passenger> result = passengerRepository.findAllByEmailIn(emails);

        assertEquals(1, result.size());
        assertEquals(passenger1.getEmail(), result.get(0).getEmail());
    }
}