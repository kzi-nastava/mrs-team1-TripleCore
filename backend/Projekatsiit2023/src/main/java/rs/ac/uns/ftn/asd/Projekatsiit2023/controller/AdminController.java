package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.ReviewDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RegisterDriverRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.DriverProfileChangeRequestService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.PanicService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.ReviewService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PanicService panicService;
    private final RideService rideService;
    private final DriverProfileChangeRequestService driverProfileChangeRequestService;

    public AdminController(PanicService panicService,
                           RideService rideService, DriverProfileChangeRequestService driverProfileChangeRequestService) {
        this.panicService = panicService;
        this.rideService = rideService;
        this.driverProfileChangeRequestService = driverProfileChangeRequestService;
    }

    @GetMapping("/rides")
    public ResponseEntity<List<RideDetailsResponse>> getAllRides() {
        List<Ride> rides = rideService.getAllRides();

        List<RideDetailsResponse> rideDetailsList = rides.stream()
                .map(rideService::createRideDetails)
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(rideDetailsList);
    }

    @GetMapping("/rides/{id}")
    public ResponseEntity<RideDetailsResponse> getRideById(@PathVariable Long id) {
        Ride ride = rideService.getRideById(id);
        RideDetailsResponse rideDetails = rideService.createRideDetails(ride);
        return ResponseEntity.ok(rideDetails);
    }

    @GetMapping("/panics")
    public List<Panic> getAllPanics() {
        return panicService.getAllPanics();
    }

    @GetMapping("/panics/active")
    public List<Panic> getActivePanics() {
        return panicService.getActivePanics();
    }

    @GetMapping("/panics/resolved")
    public List<Panic> getResolvedPanics() {
        return panicService.getResolvedPanics();
    }

    @PutMapping("/panics/{id}/resolve")
    public void resolvePanic(@PathVariable Long id) {
        panicService.markAsResolved(id);
    }

//    @PostMapping("/registerDriver")
//    public ResponseEntity<?> registerDriver(@Valid @RequestBody RegisterDriverRequest request) {
//        if (!request.getPassword().equals(request.getConfirmPassword())) {
//            return ResponseEntity.badRequest().body("Passwords do not match");
//        }
//
//        if (emailExists(request.getEmail())) {
//            return ResponseEntity.badRequest().body("Email already registered");
//        }
//
//        RegisterDriverResponse response = getRegisterDriverResponse(request);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    private static RegisterDriverResponse getRegisterDriverResponse(RegisterDriverRequest request) {
//        Long newUserId = 200L;
//
//        return new RegisterDriverResponse(
//                newUserId,
//                request.getEmail(),
//                request.getFirstName(),
//                request.getLastName(),
//                request.getAddress(),
//                request.getPhoneNumber(),
//                "default-avatar.png",
//                UserRole.DRIVER,
//                false,
//                "Registration successful! Driver can check his email for activation link",
//                request.getVehicleModel(),
//                request.getVehicleType(),
//                request.getPlateNum(),
//                request.getSeatNum(),
//                request.isBabySafe(),
//                request.isPetSafe()
//        );
//    }


    private boolean emailExists(String email) {
        return "existing@example.com".equals(email);
    }

    @GetMapping("/driver-profile-requests")
    public ResponseEntity<List<DriverProfileChangeRequestResponse>> getRequests() {
        return ResponseEntity.ok(driverProfileChangeRequestService.getAllPending());
    }

    @GetMapping("/driver-profile-requests/{id}")
    public DriverProfileChangeRequestDetailsResponse getRequestDetails(@PathVariable Long id) {
        return driverProfileChangeRequestService.getDetails(id);
    }

    @PutMapping("/driver-profile-requests/{id}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable Long id) {
        try{
            driverProfileChangeRequestService.approveRequest(id);
            return ResponseEntity.ok("Request approved successfully");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/driver-profile-requests/{id}/reject")
    public ResponseEntity<String> rejectRequest(@PathVariable Long id) {
        try{
            driverProfileChangeRequestService.rejectRequest(id);
            return ResponseEntity.ok("Request rejected successfully");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}