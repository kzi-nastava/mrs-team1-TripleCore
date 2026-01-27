package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.UserProfileResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.DriverUpdateRequestStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.DriverProfileChangeRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;

import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverProfileChangeRequestRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.UserProfileService;

import java.util.List;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;
    private final DriverProfileChangeRequestRepository requestRepository;

    public UserProfileServiceImpl(UserRepository userRepository,  DriverProfileChangeRequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
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

    @Transactional
    public DriverProfileChangeRequest createProfileChangeRequest(Long driverId, UpdateUserProfileRequest request){
        if (request.getFirstName() == null && request.getLastName() == null && request.getEmail() == null &&
                request.getAddress() == null && request.getPhone() == null && request.getProfileImage() == null) {
            throw new RuntimeException("No changes provided");
        }
        Driver driver = (Driver) userRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Driver not found"));

        List<DriverProfileChangeRequest> pendingRequests = requestRepository.findAll()
                .stream()
                .filter(r -> r.getDriverId().equals(driverId) && r.getStatus() == DriverUpdateRequestStatus.PENDING)
                .toList();

        if (!pendingRequests.isEmpty()) {
            throw new RuntimeException("You already have a pending profile change request.");
        }

        DriverProfileChangeRequest newRequest = new DriverProfileChangeRequest();
        newRequest.setDriverId(driver.getId());

        newRequest.setFirstName(request.getFirstName() != null ? request.getFirstName() : driver.getFirstName());

        newRequest.setLastName(request.getLastName() != null ? request.getLastName() : driver.getLastName());

        newRequest.setEmail(request.getEmail() != null ? request.getEmail() : driver.getEmail());

        newRequest.setAddress(request.getAddress() != null ? request.getAddress() : driver.getAddress());

        newRequest.setPhone(request.getPhone() != null ? request.getPhone() : driver.getPhone());

        newRequest.setProfileImage(request.getProfileImage() != null ? request.getProfileImage() : driver.getProfileImage());

        return requestRepository.save(newRequest);

    }


}
