package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle.ActiveRideVehicleDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle.ActiveVehicleLocationResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.ActiveVehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ActiveVehicleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RouteService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.VehicleService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.driving.DrivingSimulationService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl.RouteServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final RideService rideService;
    private final RouteServiceImpl routeService;
    private final DrivingSimulationService drivingSimulationService;
    private final ActiveVehicleRepository activeVehicleRepository;

    public VehicleController(
            VehicleService vehicleService,
            RideService rideService,
            RouteServiceImpl routeService,
            DrivingSimulationService drivingSimulationService,
            ActiveVehicleRepository activeVehicleRepository){
        this.vehicleService = vehicleService;
        this.rideService = rideService;
        this.routeService = routeService;
        this.drivingSimulationService = drivingSimulationService;
        this.activeVehicleRepository = activeVehicleRepository;
    }

    @GetMapping("/locations")
    public ResponseEntity<?> getVehicleLocations() {
        try{
            List<ActiveVehicleLocationResponse> vehicles = vehicleService.getActiveVehicleLocations();
            drivingSimulationService.moveAllIdleVehicles();
            return ResponseEntity.ok(vehicles);
        } catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed fetching active vehicles locations");
        }
    }

    @GetMapping("/active-ride/{id}")
    public ResponseEntity<?> getActiveRideVehicleTracking(@PathVariable("id") Long id){
        /* There needs to be a check if the ride is actually in progress */
        try{
            ActiveVehicle av = rideService.getActiveVehicleForRide(id);
            vehicleService.moveActiveVehicle(av.getVehicleId());

            ActiveRideVehicleDetailsResponse response =
                    vehicleService.getRideTrackingResponse(av);

            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/active-vehicle/{id}/test-route-service")
    public ResponseEntity<?> testRouteService(@PathVariable("id") Long id){
        try{
            ActiveVehicle av = vehicleService.getActiveVehicle(id);
            Ride ride = rideService.getRideById(av.getRide().getId());
            List<Location> points = routeService.convertRouteToLocationList(ride.getRoute());

            vehicleService.setRouteForActiveVehicle(id, points);
            return ResponseEntity.ok("Route added to active vehicle");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/active-vehicle/{id}/move")
    public ResponseEntity<?> moveActiveVehicle(@PathVariable("id") Long id){
        try{
            ActiveVehicle av = activeVehicleRepository.findById(id).orElseThrow();
            Location loc = vehicleService.getLocationAtRouteIndex(av.getRouteCoordinates(), av.getRouteIndex() + 1);

            av.setRouteIndex(av.getRouteIndex() + 1);
            av.setLocation(loc);

            activeVehicleRepository.saveAndFlush(av);

            ActiveRideVehicleDetailsResponse response = new ActiveRideVehicleDetailsResponse();
            response.setVehicleId(av.getVehicleId());
            response.setRideId(av.getRide().getId());
            response.setVehicleLocation(av.getLocation());
            // hardcoded for now
            response.setEstimatedTime(15L);
            response.setEstimatedDistance(200);

            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
