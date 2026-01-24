package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle.ActiveVehicleLocationResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final RideService rideService;

    public VehicleController(
            VehicleService vehicleService,
            RideService rideService){
        this.vehicleService = vehicleService;
        this.rideService = rideService;
    }

    @GetMapping("/locations")
    public ResponseEntity<?> getVehicleLocations() {
        try{
            List<ActiveVehicleLocationResponse> vehicles = vehicleService.getActiveVehicleLocations();
            return ResponseEntity.ok(vehicles);
        } catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed fetching active vehicles locations");
        }
    }

//    @GetMapping("/active-ride/{id}")
//    public ResponseEntity<?> getActiveRideVehicleTracking(@PathVariable("id") Long id){
//        Ride ride = rideService.getRideById(id);
//
//    }

}
