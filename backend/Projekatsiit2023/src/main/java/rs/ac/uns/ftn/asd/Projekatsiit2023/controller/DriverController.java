package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileChangeRequestResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.DriverUpdateRequestStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.RideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.AssignedRideResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.DriverService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<AssignedRideResponse> getNextRide(@PathVariable("id") Long id){
        AssignedRideResponse response = getMockRide();
        return ResponseEntity.ok(response);
    }

    private AssignedRideResponse getMockRide(){
        RideDTO ride = new RideDTO();
        ride.setPickup("Bulevar Oslobođenja 45, Novi Sad");
        ride.setDropoff("Trg Slobode 1, Novi Sad");
        ride.setEstimatedTime(18);
        ride.setEstimatedDistance(6.4);
        ride.setEstimatedPrice(820.0);
        ride.setDriverId(null);
        ride.setPassengerIds(List.of(1L, 2L, 3L));

        AssignedRideResponse response = new AssignedRideResponse();
        response.setHasAssignedRide(true);
        response.setRide(ride);
        return response;
    }

    @GetMapping("/{id}/ride-history")
    public ResponseEntity<?> getRideHistory(@PathVariable("id") Long id){
        List<RideDTO> rides = getMockRides();
        List<RideDTO> response = new ArrayList<>();

        for(RideDTO ride : rides){
            if (Objects.equals(ride.getDriverId(), id)) { response.add(ride); }

        }

        return ResponseEntity.ok(response);
    }

    private List<RideDTO> getMockRides() {
        RideDTO ride1 = new RideDTO();
        ride1.setPickup("Bulevar Oslobođenja 45, Novi Sad");
        ride1.setDropoff("Trg Slobode 1, Novi Sad");
        ride1.setEstimatedTime(18);
        ride1.setEstimatedDistance(6.4);
        ride1.setEstimatedPrice(820.0);
        ride1.setDriverId(1L);
        ride1.setPassengerIds(List.of(1L, 2L));

        RideDTO ride2 = new RideDTO();
        ride2.setPickup("Narodnog Fronta 12, Novi Sad");
        ride2.setDropoff("Železnička stanica, Novi Sad");
        ride2.setEstimatedTime(12);
        ride2.setEstimatedDistance(4.1);
        ride2.setEstimatedPrice(520.0);
        ride2.setDriverId(1L);
        ride2.setPassengerIds(List.of(3L));

        RideDTO ride3 = new RideDTO();
        ride3.setPickup("Futoški put 25, Novi Sad");
        ride3.setDropoff("SPENS, Novi Sad");
        ride3.setEstimatedTime(20);
        ride3.setEstimatedDistance(7.8);
        ride3.setEstimatedPrice(930.0);
        ride3.setDriverId(2L);
        ride3.setPassengerIds(List.of(4L, 5L));

        return List.of(ride1, ride2, ride3);
    }

    @PostMapping("/create")
    public ResponseEntity<?> CreateTestDriver(){
        driverService.createDriverWithVehicle();
        return ResponseEntity.ok("Test driver created");
    }
}