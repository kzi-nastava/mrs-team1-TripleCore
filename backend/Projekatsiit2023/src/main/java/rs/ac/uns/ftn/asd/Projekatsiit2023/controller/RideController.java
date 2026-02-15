package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideCancelResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideEstimateResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideFinishResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideStopResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.CancelerType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.VehicleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/rides")
public class  RideController {

    private final RideService rideService;
    private final RouteService routeService;
    private final RideCancelService rideCancelService;
    private final RideStopService rideStopService;
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;
    private final NotificationService notificationService;

    public RideController(RideService rideService,
                          RouteService routeService,
                          RideCancelService rideCancelService,
                          RideStopService rideStopService, VehicleService vehicleService, VehicleRepository vehicleRepository,
                          NotificationService notificationService){
        this.rideService = rideService;
        this.routeService = routeService;
        this.rideCancelService = rideCancelService;
        this.rideStopService = rideStopService;
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
        this.notificationService = notificationService;
    }

    @PostMapping("/estimate")
    public ResponseEntity<RideEstimateResponse> estimateRoute(
            @Valid @RequestBody RideEstimateRequest request) {

        RideEstimateResponse response = routeService.calculateRoute(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRide(
            @PathVariable("id") Long id,
            @Valid @RequestBody RideCancelRequest request) {

        try {
            RideCancelResponse response = rideCancelService.cancelRide(id, request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error cancelling ride: " + e.getMessage());
        }
    }

    @PostMapping("/{rideId}/panic")
    public ResponseEntity<?> activatePanic(
            @PathVariable Long rideId,
            @RequestParam Long userId) {

        try {
            rideService.activatePanic(rideId, userId);
            return ResponseEntity.ok("Panic activated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<?> stopRide(
            @PathVariable("id") Long rideId,
            @Valid @RequestBody RideStopRequest request) {

        try {
            RideStopResponse response = rideStopService.stopRide(rideId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error stopping ride: " + e.getMessage());
        }
    }

    @PostMapping ("/{id}/finish")
    public ResponseEntity<?> finishRide(
            @PathVariable("id") Long id){

        try{
            Ride ride = rideService.getRideById(id);
            rideService.finishRide(ride.getId());
            return ResponseEntity.ok("Ride finished successfully");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/test-notifications")
    public ResponseEntity<?> testNotifications(@PathVariable("id") Long id){
        Ride ride = rideService.getRideById(id);
        notificationService.rideFinishNotifyPassengers(ride);
        return ResponseEntity.ok("Email sent");
    }


    @PostMapping
    public ResponseEntity<RideResponse> orderRide(
            @Valid @RequestBody RideRequest request,
            @RequestHeader(value = "X-User-Email", required = true) String userEmail) {

        try {
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity.status(400).body(null);
            }

            System.out.println("Received rideRequest:");
            System.out.println(request);
            System.out.println("User email from header: " + userEmail);


            RideResponse rideResponse = rideService.orderRide(request, userEmail);
            System.out.println("Ride ordered successfully: " + rideResponse);

            return ResponseEntity.ok(rideResponse);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


//    @PostMapping("/favorites")
//    public ResponseEntity<FavoriteRouteResponse> addFavoriteRoute(
//            @Valid @RequestBody FavoriteRouteRequest request) {
//
//        Long routeId = 101L;
//
//        FavoriteRouteResponse response = new FavoriteRouteResponse(
//                routeId,
//                request.getStartLocation(),
//                request.getEndLocation(),
//                "Route added to favorites"
//        );
//
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/favorites/{userId}")
//    public ResponseEntity<List<FavoriteRouteResponse>> getFavoriteRoutes(@PathVariable Long userId) {
//
//        List<FavoriteRouteResponse> favorites = List.of(
//                new FavoriteRouteResponse(101L, "Beograd, Nemanjina 1", "Beograd, Bulevar Kralja Aleksandra 10", "Favorite route 1"),
//                new FavoriteRouteResponse(102L, "Beograd, Studentski trg 5", "Beograd, Trg Slavija 2", "Favorite route 2")
//        );
//        return ResponseEntity.ok(favorites);
//    }

    @PostMapping("/{rideId}/start")
    public ResponseEntity<?> startRide(
            @PathVariable Long rideId,
            @RequestHeader(value = "X-User-Id", required = true) Long driverId) {

        try {
            rideService.startRide(driverId, rideId);

            return ResponseEntity.ok("Ride started successfully");

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error starting ride: " + e.getMessage());
        }
    }

//     Database test
    @PostMapping("/create")
    public ResponseEntity<?> createTestRide(){
        try{
//            testService.generateMockRides();
            return ResponseEntity.ok("Ride created");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(e.getMessage());
        }
    }

    @GetMapping("/ride-details/{id}")
    public ResponseEntity<?> getRideById(@PathVariable("id") Long id){
        try{
            Ride ride = rideService.getRideById(id);
            return ResponseEntity.ok(rideService.createRideDetails(ride));
        }
        catch (EntityNotFoundException nfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nfe.getMessage());
        }
    }

    @GetMapping("/to-start/{driverId}")
    public ResponseEntity<?> getRideToStart(@PathVariable Long driverId) {
        try {
            Ride ride = rideService.getRideToStartForDriver(driverId);
            if (ride == null) {
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.ok(rideService.createRideDetails(ride));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}