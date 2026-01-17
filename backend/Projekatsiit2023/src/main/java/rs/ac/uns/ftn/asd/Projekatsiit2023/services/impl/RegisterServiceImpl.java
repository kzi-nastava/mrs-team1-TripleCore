package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RegisterRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RegisterResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Admin;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RegisterService;

import java.time.LocalDateTime;

@Service
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public RegisterServiceImpl(UserRepository userRepository,
                               EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        // check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // check if passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        // creating new user based on role
        User user = createUserByRole(request);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhoneNumber());
        user.setRole(request.getRole());
        user.setAccountActivated(false);
        user.setCreatedAt(LocalDateTime.now());

        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            user.setProfileImage(request.getProfileImage());
        } else {
            user.setProfileImage("/icons/profile.png"); // Default URL
        }

        // save user to database
        User savedUser = userRepository.save(user);

        // Generate activation link
        String activationLink = generateActivationLink(savedUser.getId());

        // Send activation email
        emailService.sendActivationEmail(savedUser.getEmail(), activationLink);

        return createRegisterResponse(savedUser);
    }

    public String generateActivationLink(Long userId) {
        return "http://localhost:8080/api/auth/activate?userId=" + userId;
    }

    private User createUserByRole(RegisterRequest request) {
        return switch (request.getRole()) {
            case PASSENGER -> new Passenger();
            case DRIVER -> new Driver();
            case ADMIN -> new Admin();
            default -> throw new IllegalArgumentException("Unknown role: " + request.getRole());
        };
    }

    private RegisterResponse createRegisterResponse(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getPhone(),
                user.getProfileImage(),
                user.getRole(),
                user.isAccountActivated(),
                "Registration successful! Please check your email for the activation link."
        );
    }
}