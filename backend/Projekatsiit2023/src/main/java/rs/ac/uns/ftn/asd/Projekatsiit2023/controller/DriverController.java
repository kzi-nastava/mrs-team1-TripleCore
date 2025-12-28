package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.RideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.AssignedRideResponse;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

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

    @GetMapping("/{id}/next-ride")
    public ResponseEntity<AssignedRideResponse> getNextRide(@PathVariable("id") Long id){
        AssignedRideResponse response = getMockRide();
        return ResponseEntity.ok(response);
    }

    private AssignedRideResponse getMockRide(){
        RideDTO ride = new RideDTO();
        ride.setPickup("Nemanjina 4, Belgrade");
        ride.setDropoff("Knez Mihailova 12, Belgrade");
        ride.setEstimatedTime(15);
        ride.setEstimatedDistance(5.2);
        ride.setEstimatedPrice(750.0);
        ride.setPassengerId(1L);

        AssignedRideResponse response = new AssignedRideResponse();
        response.setHasAssignedRide(true);
        response.setRide(ride);
        return response;
    }
}