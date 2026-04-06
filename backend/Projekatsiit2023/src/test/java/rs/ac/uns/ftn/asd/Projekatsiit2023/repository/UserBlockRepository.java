package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.UserBlock;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserBlockRepositoryTest {

    @Autowired
    private UserBlockRepository userBlockRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = createUser("blockeduser@example.com", "Blocked User");
    }

    private User createUser(String email, String name) {
        Passenger p = new Passenger();

        p.setEmail(email);
        p.setFirstName(name);
        p.setLastName("Test prezime");
        p.setPassword("password");
        p.setAddress("Test Adresa");
        p.setPhone("123456789");
        p.setRole(UserRole.PASSENGER);
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now());

        return entityManager.persistAndFlush(p);
    }

    private UserBlock createUserBlock(User user) {
        UserBlock block = new UserBlock();
        block.setUserId(user.getId());
        block.setNote("Test note");
        return entityManager.persistAndFlush(block);
    }

    @Test
    void findByUserId_shouldReturnUserBlock_whenExists() {
        UserBlock block = createUserBlock(user);

        Optional<UserBlock> found = userBlockRepository.findByUserId(user.getId());

        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getUserId());
        assertEquals("Test note", found.get().getNote());
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenNotExists() {
        Optional<UserBlock> found = userBlockRepository.findByUserId(999L);
        assertTrue(found.isEmpty());
    }
}