package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideCancelResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideEstimateResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideFinishResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideStopResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.CancelerType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.test.TestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/rides")
public class  RideController {

    private final RideService rideService;
    private final TestService testService;

    public RideController(RideService rideService, TestService testService){
        this.rideService = rideService;
        this.testService = testService;
    }

    @PostMapping("/estimate")
    public ResponseEntity<RideEstimateResponse> estimateRide(
            @Valid @RequestBody RideEstimateRequest request) {

        Double distance = calculateDistance(request.getStartAddress(), request.getEndAddress());

        Integer time = calculateEstimatedTime(distance);

        Double price = calculatePriceBySpecification(distance, request.getVehicleType().name());

        String route = generateRouteCoordinates();

        RideEstimateResponse response = new RideEstimateResponse(
                price,
                time,
                distance,
                route,
                String.format("Ride estimate: %.0f RSD for %.1f km, estimated time: %d min",
                        price, distance, time)
        );

        return ResponseEntity.ok(response);
    }

    private Double calculateDistance(String start, String end) {
        return 7.5;
    }

    private Integer calculateEstimatedTime(Double distance) {
        return 5 + (int)(distance * 2);
    }

    /**
     * cena_po_tipu_vozila + broj_kilometara * 120
     */
    private Double calculatePriceBySpecification(Double distance, String vehicleType) {
        double basePricePerVehicleType = getBasePriceForVehicleType(vehicleType);

        return basePricePerVehicleType + (distance * 120.0);
    }

    private double getBasePriceForVehicleType(String vehicleType) {
        if (vehicleType == null) {
            return 300.0; // default STANDARD
        }

        return switch (vehicleType.toUpperCase()) {
            case "STANDARD" -> 300.0;
            case "LUXURY" -> 800.0;
            case "VAN" -> 600.0;
            default -> 300.0;
        };
    }

    private String generateRouteCoordinates() {
        return "44.7866,20.4489;44.8125,20.4612;44.8150,20.4630";
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRide(
            @PathVariable("id") Long id,
            @Valid @RequestBody RideCancelRequest request) {

        if (!rideExists(id)) {
            return ResponseEntity.status(404)
                    .body("Ride with ID " + id + " not found");
        }

        CancelerType cancelerType = request.getCancelerType();
        String reason = request.getReason();

        if (cancelerType == CancelerType.DRIVER && (reason == null || reason.trim().isEmpty())) {
            return ResponseEntity.badRequest()
                    .body("Driver must provide a cancellation reason");
        }

        if (cancelerType == CancelerType.PASSENGER) {
            if (!canPassengerCancel(id)) {
                return ResponseEntity.badRequest()
                        .body("Passenger can only cancel 10 minutes before ride start");
            }
        }

        boolean success = true;

        RideCancelResponse response = new RideCancelResponse(success, cancelerType, reason);

        return ResponseEntity.ok(response);
    }

    private boolean canPassengerCancel(Long id) {
        return id >= 3;
    }

    private boolean rideExists(Long id) {
        return id >= 1 && id <= 5;
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<?> stopRide(
            @PathVariable("id") Long id,
            @Valid @RequestBody RideStopRequest request) {

        if (!rideExists(id)) {
            return ResponseEntity.status(404)
                    .body("Ride with ID " + id + " not found");
        }

        if (!isRideInProgress(id)) {
            return ResponseEntity.badRequest()
                    .body("Ride is not in progress. Only rides in progress can be stopped.");
        }

        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();
        String address = request.getAddress();

        Double originalPrice = getOriginalPrice(id);
        Double originalDistance = getOriginalDistance(id);
        Double newDistance = calculateNewDistance(id, latitude, longitude);
        Double newPrice = recalculatePrice(originalPrice, originalDistance, newDistance);

        updateRideWithNewDestination(id, address, latitude, longitude, newPrice, newDistance);

        RideStopResponse response = new RideStopResponse(
                true,
                String.format("Ride #%d stopped successfully at %s", id, address),
                newPrice,
                newDistance,
                LocalDateTime.now(),
                address
        );

        return ResponseEntity.ok(response);
    }

    private boolean isRideInProgress(Long id) {
        return id == 2 || id == 4;
    }

    private Double getOriginalPrice(Long id) {
        return 1500.0;
    }

    private Double getOriginalDistance(Long id) {
        return 7.5;
    }

    private Double calculateNewDistance(Long id, Double lat, Double lng) {
        return 5.2;
    }

    private Double recalculatePrice(Double originalPrice, Double originalDistance, Double newDistance) {
        double pricePerKm = originalPrice / originalDistance;
        return pricePerKm * newDistance;
    }

    private void updateRideWithNewDestination(Long id, String address,
                                              Double lat, Double lng,
                                              Double newPrice, Double newDistance) {
        System.out.printf("Ride #%d updated - New destination: %s (%.6f, %.6f), " +
                        "New price: %.2f, New distance: %.2f km%n",
                id, address, lat, lng, newPrice, newDistance);
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

//    @PatchMapping("/{id}/start")
//    public ResponseEntity<?> finishRide(
//            @PathVariable("id") Long id,
//            @Valid @RequestBody RideStartRequest request){
//
//    }


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

        // calucating price = basePrice + distance * 120
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
            testService.generateMockRides();
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