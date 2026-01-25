package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.UserProfileResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl.EmailService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserProfileServiceImpl(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public UserRole getCurrentUserRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        switch (user.getRole().toString()) {
            case "DRIVER" -> {
                return UserRole.DRIVER;
            }
            case "ADMIN" -> {
                ;
                return UserRole.ADMIN;
            }
            case "PASSENGER" -> {
                return UserRole.PASSENGER;
            }
            default -> throw new RuntimeException("Invalid user role");
        }
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getPhone(),
                user.getProfileImage()
        );
    }

    @Override
    public DriverProfileResponse getDriverProfile(Long driverId) {
        User user = userRepository.findById(driverId).orElseThrow(() -> new RuntimeException("User not found"));


        if (!(user instanceof Driver)) {
            throw new RuntimeException("User with ID " + driverId + " is not a driver");
        }

        Driver driver = (Driver) user;

        return new DriverProfileResponse(
                driver.getId(),
                driver.getEmail(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getAddress(),
                driver.getPhone(),
                driver.getProfileImage(),
                driver.getWorkingHoursToday(),
                driver.getVehicle()
                );
    }

    @Override
    public void updateProfile(Long userId, UpdateUserProfileRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (user instanceof Driver) {
            throw new RuntimeException("Drivers are not allowed to update profile via this method");
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }

            user.setEmail(request.getEmail());
        }

        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }

        System.out.println("=== UPDATE USER PROFILE (TEST MODE) ===");
        System.out.println("User profile updated: " + user);
        System.out.println("ID: " + user.getId());
        System.out.println("First name: " + user.getFirstName());
        System.out.println("Last name: " + user.getLastName());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Phone: " + user.getPhone());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Profile image: " + user.getProfileImage());
        System.out.println("======================================");

        userRepository.save(user);

    }

    @Override
    public void changePassword(Long userId, String newPassword) {

    }
}
