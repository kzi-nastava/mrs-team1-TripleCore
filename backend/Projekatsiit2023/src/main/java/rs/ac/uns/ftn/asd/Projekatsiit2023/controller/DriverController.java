package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileChangeRequestResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.DriverUpdateRequestStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.DriverService;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<?> changeDriverAvailability(
            @PathVariable("id") Long id,
            @RequestParam("available") boolean available) {

        if (id != 1L) {
            return ResponseEntity.badRequest().body("Only drivers can change availability");
        }

        boolean hasActiveRide = Math.random() > 0.5;

        if (!available && hasActiveRide) {
            return ResponseEntity.ok(
                    "You have an active ride. You will become unavailable AFTER the ride finishes.\n" +
                            "System will not offer you to new passengers until you become available again."
            );
        }

        return ResponseEntity.ok("Driver availability updated to: " + available);
    }

    @PutMapping("/profile")
    public ResponseEntity<DriverProfileChangeRequestResponse> requestProfileUpdate(
            @RequestBody UpdateUserProfileRequest request) {

        DriverProfileChangeRequestResponse response =
                new DriverProfileChangeRequestResponse(
                        1L,
                        41L,
                        request,
                        DriverUpdateRequestStatus.PENDING
                );

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }



    @GetMapping("/{id}/next-ride")
    public ResponseEntity<?> getNextRide(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> GetDriver(@PathVariable("id") Long id){
        Driver driver;
        try{
            driver = driverService.getDriverById(id);
        }
        catch (EntityNotFoundException nfe){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nfe.getMessage());
        }
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/{id}/ride-history")
    public ResponseEntity<?> GetDriverRideHistory(@PathVariable Long id){
        List<RideDetailsResponse> response =  driverService.getRideHistory(id);
        return ResponseEntity.ok(response);
    }
}