package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.AccountActivationService;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AccountActivationServiceImpl implements AccountActivationService {

    private final UserRepository userRepository;

    public AccountActivationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean activateUser(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.isAccountActivated()) {
                return true; // Already activated
            }

            // Check if 24 hours have passed since registration
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = user.getCreatedAt();

            long hoursPassed = Duration.between(createdAt, now).toHours();

            if (hoursPassed <= 24) {
                user.setAccountActivated(true);
                userRepository.save(user);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.err.println("Activation failed for userId " + userId + ": " + e.getMessage());
            return false;
        }
    }
}