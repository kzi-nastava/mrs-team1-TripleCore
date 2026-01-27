package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileChangeRequestDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileChangeRequestResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileSnapshot;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.DriverUpdateRequestStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.DriverProfileChangeRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverProfileChangeRequestRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.DriverProfileChangeRequestService;


import java.util.List;

@Service
public class DriverProfileChangeRequestServiceImpl implements DriverProfileChangeRequestService {
    private final DriverProfileChangeRequestRepository repository;
    private final UserRepository userRepository;

    public DriverProfileChangeRequestServiceImpl(DriverProfileChangeRequestRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<DriverProfileChangeRequestResponse> getAllPending() {
        return repository
                .findAllByStatusOrderByCreatedAtDesc(DriverUpdateRequestStatus.PENDING)
                .stream()
                .map(DriverProfileChangeRequestResponse::new)
                .toList();
    }

    public DriverProfileChangeRequestDetailsResponse getDetails(Long requestId) {

        if (requestId == null) {
            throw new RuntimeException("Request ID cannot be null");
        }

        DriverProfileChangeRequest req = repository.findById(requestId).
                orElseThrow(() -> new RuntimeException("Request not found"));

        Driver driver = (Driver) userRepository.findById(req.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        DriverProfileSnapshot currentProfile = new DriverProfileSnapshot(
                driver.getFirstName(),
                driver.getLastName(),
                driver.getAddress(),
                driver.getPhone(),
                driver.getEmail(),
                driver.getProfileImage()
        );

        DriverProfileSnapshot requestedProfile = new DriverProfileSnapshot(
                req.getFirstName(),
                req.getLastName(),
                req.getAddress(),
                req.getPhone(),
                req.getEmail(),
                req.getProfileImage()
        );

        return new DriverProfileChangeRequestDetailsResponse(
                req.getId(),
                currentProfile,
                requestedProfile,
                req.getStatus(),
                req.getStatusUpdatedAt()
        );

    }

    public void approveRequest(Long requestId) {
        DriverProfileChangeRequest req = repository.findById(requestId).
                orElseThrow(() -> new RuntimeException("Request not found"));

        if (req.getStatus() != DriverUpdateRequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be approved");
        }

        Driver driver = (Driver) userRepository.findById(req.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));


        driver.setFirstName(req.getFirstName());
        driver.setLastName(req.getLastName());
        driver.setAddress(req.getAddress());
        driver.setPhone(req.getPhone());
        driver.setEmail(req.getEmail());
        driver.setProfileImage(req.getProfileImage());

        userRepository.save(driver);


        req.setStatus(DriverUpdateRequestStatus.APPROVED);
        req.setStatusUpdatedAt(java.time.LocalDateTime.now());

        repository.save(req);

        System.out.println("=== APPROVE DRIVER PROFILE CHANGE REQUEST (TEST MODE) ===");
        System.out.println("Driver profile change request approved: " + req);
        System.out.println("Driver ID: " + driver.getId());
        System.out.println("New First name: " + driver.getFirstName());
        System.out.println("New Last name: " + driver.getLastName());
        System.out.println("New Address: " + driver.getAddress());
        System.out.println("New Phone: " + driver.getPhone());
        System.out.println("New Email: " + driver.getEmail());
        System.out.println("New Profile image: " + driver.getProfileImage());
        System.out.println("======================================");

    }

    public void rejectRequest(Long requestId) {
        DriverProfileChangeRequest req = repository.findById(requestId).
                orElseThrow(() -> new RuntimeException("Request not found"));

        if (req.getStatus() != DriverUpdateRequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be rejected");
        }

        req.setStatus(DriverUpdateRequestStatus.REJECTED);
        req.setStatusUpdatedAt(java.time.LocalDateTime.now());

        System.out.println( "=== REJECT DRIVER PROFILE CHANGE REQUEST (TEST MODE) ===");
        System.out.println("Driver profile change request rejected: " + req);
        System.out.println("Request ID: " + req.getId());
        System.out.println("Driver ID: " + req.getDriverId());
        System.out.println("======================================");
        repository.save(req);
    }

}
