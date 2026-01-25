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
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideCancelService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideStopService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RouteService;

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

    public RideController(RideService rideService,
                          RouteService routeService,
                          RideCancelService rideCancelService,
                          RideStopService rideStopService){
        this.rideService = rideService;
        this.routeService = routeService;
        this.rideCancelService = rideCancelService;
        this.rideStopService = rideStopService;
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

    private boolean isRideInProgress(Long id) {
        return id == 2 || id == 4;
    }

    private boolean rideExists(Long id) {
        return id >= 1 && id <= 5;
    }

    @PostMapping ("/{id}/finish")
    public ResponseEntity<?> finishRide(
            @PathVariable("id") Long id,
            @Valid @RequestBody RideFinishRequest request){

        if (!rideExists(id)) {
            return ResponseEntity.status(404)
                    .body("Ride with ID " + id + " not found");
        }

        if (!isRideInProgress(id)) {
            return ResponseEntity.badRequest()
                    .body("Ride is not in progress. Only rides in progress can be finished.");
        }

        // did the price change
        boolean priceChanged = getRandomBoolean();
        double newPrice = 0;
        if (priceChanged) newPrice = 1500;

        RideFinishResponse response = new RideFinishResponse(
                String.format("Ride #%d finished successfully", id),
                priceChanged,
                newPrice
        );

        return ResponseEntity.ok(response);
    }

    public boolean getRandomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    @PostMapping
    public ResponseEntity<RideResponse> orderRide(@Valid @RequestBody RideRequest request) {


        boolean hasActiveDrivers = true;
        boolean driversBusy = false;
        boolean driverOverworked = false;


        if (!hasActiveDrivers) {
            System.out.println("notification: No active drivers available");
            return ResponseEntity.ok(new RideResponse(null, RideStatus.REJECTED, 0, null,
                    "notification: there are no active drivers at the moment"));
        }

        if (driversBusy) {
            System.out.println("notification: all drivers are currently busy");
            return ResponseEntity.ok(new RideResponse(null, RideStatus.REJECTED, 0, null,
                    "notification: no driver available at the moment"));
        }


        if (driverOverworked) {
            System.out.println("notification: driver is currently busy");
            return ResponseEntity.ok(new RideResponse(null, RideStatus.REJECTED, 0, null,
                    "notification: no driver available at the moment"));
        }


        if (request.getScheduledTime() != null &&
                request.getScheduledTime().isAfter(LocalDateTime.now().plusHours(5))) {
            return ResponseEntity.badRequest().body(new RideResponse(null, RideStatus.REJECTED, 0, null,
                    "notification: scheduled rides can only be booked up to 5 hours in advance"));
        }

        double basePrice = request.getVehicleType() != null ? switch (request.getVehicleType()) {
            case STANDARD -> 300;
            case VAN -> 500;
            case LUXURY -> 800;
        } : 300; // default
        double price = basePrice + request.getDistanceInKm() * 120;


        Long driverId = 50L;
        Long rideId = 100L;


        System.out.println("notification: new ride for passenger");
        System.out.println("notification: mew ride for driver id " + driverId);

        if (request.getScheduledTime() != null) {
            System.out.println("reminder: scheduled ride in 5 hours for driver id " + driverId);
        }

        return ResponseEntity.ok(new RideResponse(rideId, RideStatus.ACCEPTED, price, driverId,
                "notification: ride accepted"));
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteRouteResponse> addFavoriteRoute(
            @Valid @RequestBody FavoriteRouteRequest request) {

        Long routeId = 101L;

        FavoriteRouteResponse response = new FavoriteRouteResponse(
                routeId,
                request.getStartLocation(),
                request.getEndLocation(),
                "Route added to favorites"
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/favorites/{userId}")
    public ResponseEntity<List<FavoriteRouteResponse>> getFavoriteRoutes(@PathVariable Long userId) {

        List<FavoriteRouteResponse> favorites = List.of(
                new FavoriteRouteResponse(101L, "Beograd, Nemanjina 1", "Beograd, Bulevar Kralja Aleksandra 10", "Favorite route 1"),
                new FavoriteRouteResponse(102L, "Beograd, Studentski trg 5", "Beograd, Trg Slavija 2", "Favorite route 2")
        );
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{rideId}/start") public ResponseEntity<String> startRide(@PathVariable Long rideId) {
        System.out.println("Ride " + rideId + " started");
        return ResponseEntity.ok("Ride started");
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getRideById(@PathVariable("id") Long id){
        try{
            Ride ride = rideService.getRideById(id);
            return ResponseEntity.ok(ride);
        }
        catch (EntityNotFoundException nfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nfe.getMessage());
        }
    }


}