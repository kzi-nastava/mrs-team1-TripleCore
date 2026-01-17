package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.LoginRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.LoginResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final RideRepository rideRepository;
    private final EmailService emailService;

    public LoginServiceImpl(UserRepository userRepository,
                            RideRepository rideRepository,
                            EmailService emailService) {
        this.userRepository = userRepository;
        this.rideRepository = rideRepository;
        this.emailService = emailService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        // find user by email
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check if account is activated and not blocked
        if (!user.isAccountActivated()) {
            throw new RuntimeException("Account is not activated");
        }

        if (user.isAccountBlocked()) {
            throw new RuntimeException("Account is blocked");
        }

        // check password
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // temporary generate fake JWT token
        String fakeToken = "FAKE-JWT-TOKEN-" + user.getId();

        // if user is driver, set driverAvailable to true
        boolean driverAvailable = user.getRole() == UserRole.DRIVER;

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName(),
                fakeToken,
                driverAvailable
        );
    }

    @Override
    public String initiatePasswordReset(String email) {
        // find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with this email does not exist"));

        // check if account is activated and not blocked
        if (!user.isAccountActivated()) {
            throw new RuntimeException("Account is not activated");
        }

        if (user.isAccountBlocked()) {
            throw new RuntimeException("Account is blocked");
        }

        // generate reset link
        String resetLink = "http://localhost:4200/reset-password?userId=" + user.getId();

        // send link via email
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        return "Password reset link has been sent to: " + email;
    }

    @Override
    public String resetPassword(Long userId, String newPassword) {
        // find user by id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isAccountActivated()) {
            throw new RuntimeException("Account is not activated");
        }

        if (user.isAccountBlocked()) {
            throw new RuntimeException("Account is blocked");
        }

        // update password
        user.setPassword(newPassword);
        userRepository.save(user);

        return "Password has been successfully reset.";
    }

    @Override
    public String logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == UserRole.DRIVER) {
            Driver driver = (Driver) user;

            // if driver has ride in progress, he cannot logout
            boolean hasActiveRide = rideRepository.existsByDriverAndStatus(driver, RideStatus.IN_PROGRESS);

            if (hasActiveRide) {
                throw new RuntimeException("Cannot logout while having an active ride.");
            }

            // if no active ride, set driverAvailable to false
            driver.setAvailable(false);
            userRepository.save(driver);

            System.out.println("Driver " + driver.getEmail() + " logged out and set to UNAVAILABLE");
        } else {
            // for other users, just log out
            System.out.println("User " + user.getEmail() + " logged out");
        }

        return "Successfully logged out";
    }
}
