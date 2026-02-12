package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.ReviewDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle.ActiveRideVehicleDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl.RouteServiceImpl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final RouteRepository routeRepository;
    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final ActiveVehicleRepository activeVehicleRepository;

    private final PanicService panicService;
    private final VehicleService vehicleService;
    private final RouteServiceImpl routeService;
    private final NotificationService notificationService;


    public RideService(
            RideRepository rideRepository,
            DriverRepository driverRepository,
            PassengerRepository passengerRepository,
            RouteRepository routeRepository,
            ReviewService reviewService,
            UserRepository userRepository,
            PanicService panicService,
            VehicleService vehicleService,
            RouteServiceImpl routeService,
            ActiveVehicleRepository activeVehicleRepository,
            NotificationService notificationService
    ) {
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
        this.passengerRepository = passengerRepository;
        this.routeRepository = routeRepository;
        this.reviewService = reviewService;
        this.userRepository = userRepository;
        this.panicService = panicService;
        this.vehicleService = vehicleService;
        this.routeService = routeService;
        this.activeVehicleRepository = activeVehicleRepository;
        this.notificationService = notificationService;
    }

    public List<Ride> getAllRides(){
        return rideRepository.findAll();
    }

    public Ride getRideById(Long id){
        return rideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ride with id: " + id + " not found"));
    }

    public List<Ride> getDriverRides(Long driverId){
        try{
            return rideRepository.findByDriverId(driverId);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void activatePanic(Long rideId, Long userId) {
        // find a ride by id
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        // check if panic is already activated
        if (ride.isPanic()) {
            throw new RuntimeException("Panic is already activated for this ride");
        }

        // check if user is part of the ride
        if (!isUserInRide(userId, ride)) {
            throw new RuntimeException("User is not part of this ride");
        }

        // check if ride is IN_PROGRESS
        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new RuntimeException("Cannot activate panic for this ride");
        }

        // user who triggered panic
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // activate panic
        ride.setPanic(true);
        ride.setPanicTriggeredBy(user);
        ride.setPanicTriggeredAt(LocalDateTime.now());
        rideRepository.save(ride);

        Panic panic = new Panic();
        panic.setDriverName(ride.getDriver() != null ?
                ride.getDriver().getFirstName() + " " + ride.getDriver().getLastName() : "Unknown");
        panic.setPassengerName(ride.getOrderer().getFirstName() + " " + ride.getOrderer().getLastName());
        panic.setTime(LocalDateTime.now());
        panic.setVehicle(ride.getDriver() != null && ride.getDriver().getVehicle() != null ?
                ride.getDriver().getVehicle().getBrand() + " " + ride.getDriver().getVehicle().getModel() : "Unknown");
        panic.setLocation(ride.getRoute() != null ? String.valueOf(ride.getRoute().getStartLocation()) : "Unknown");
        panic.setLicensePlate(ride.getDriver() != null && ride.getDriver().getVehicle() != null ?
                ride.getDriver().getVehicle().getPlateNumber() : null);

        panicService.createPanic(panic);
    }

    private boolean isUserInRide(Long userId, Ride ride) {
        // is user the orderer
        if (ride.getOrderer().getId().equals(userId)) {
            return true;
        }

        // is user the driver
        if (ride.getDriver() != null && ride.getDriver().getId().equals(userId)) {
            return true;
        }

        // is user a linked passenger
        return ride.getLinkedPassengers().stream()
                .anyMatch(passenger -> passenger.getId().equals(userId));
    }

    public RideDetailsResponse createRideDetails(Ride ride) {
        try {
            RideDetailsResponse rideDetails = new RideDetailsResponse();

            // ride id
            rideDetails.setId(ride.getId());

            // Passengers
            if (ride.getOrderer() != null) {
                rideDetails.setOrdererName(
                        ride.getOrderer().getFirstName() + " " + ride.getOrderer().getLastName()
                );

                rideDetails.setOrdererProfileImage(ride.getOrderer().getProfileImage());
            }

            if (ride.getLinkedPassengers() != null) {
                rideDetails.setLinkedPassengers(
                        ride.getLinkedPassengers()
                                .stream()
                                .filter(p -> p != null)
                                .map(p -> p.getFirstName() + " " + p.getLastName())
                                .toList()
                );
            } else {
                rideDetails.setLinkedPassengers(new ArrayList<>());
            }

            // Driver
            if (ride.getDriver() != null) {
                rideDetails.setDriverName(
                        ride.getDriver().getFirstName() + " " + ride.getDriver().getLastName()
                );

                // Vehicle - sa null check
                if (ride.getDriver().getVehicle() != null) {
                    String brand = ride.getDriver().getVehicle().getBrand() != null ?
                            ride.getDriver().getVehicle().getBrand() : "";
                    String model = ride.getDriver().getVehicle().getModel() != null ?
                            ride.getDriver().getVehicle().getModel() : "";
                    rideDetails.setVehicle(brand + " " + model);
                } else {
                    rideDetails.setVehicle("No vehicle assigned");
                }

                // Driver picture
                rideDetails.setDriverProfileImage(ride.getDriver().getProfileImage());
            }

            // Route
            if (ride.getRoute() != null) {
                rideDetails.setStartLocation(ride.getRoute().getStartLocation());
                rideDetails.setEndLocation(ride.getRoute().getEndLocation());

                if (ride.getRoute().getStops() != null) {
                    rideDetails.setRouteStops(
                            ride.getRoute().getStops()
                                    .stream()
                                    .filter(stop -> stop != null && stop.getLocation() != null)
                                    .map(RouteStop::getLocation)
                                    .toList()
                    );
                } else {
                    rideDetails.setRouteStops(new ArrayList<>());
                }
            }

            // Time
            rideDetails.setStartTime(ride.getStartTime());
            rideDetails.setEndTime(ride.getEndTime());

            // Panic
            rideDetails.setPanic(ride.isPanic());
            if (ride.getPanicTriggeredBy() != null) {
                rideDetails.setPanicTriggeredBy(
                        ride.getPanicTriggeredBy().getFirstName() + " " + ride.getPanicTriggeredBy().getLastName()
                );
            }
            rideDetails.setPanicTriggeredAt(ride.getPanicTriggeredAt());
            rideDetails.setPanicTriggeredAt(ride.getPanicTriggeredAt());

            // Other info
            rideDetails.setPrice(ride.getPrice() != null ? ride.getPrice() : 0.0);
            rideDetails.setStatus(ride.getStatus());
            if (ride.getCancelledBy() != null) {
                rideDetails.setCancelledBy(ride.getCancelledBy().getRole());
            }
            rideDetails.setInconsistencies(ride.getInconsistencies());

            // Reviews
            List<ReviewDTO> dtos = new ArrayList<>();
            for(Review review : reviewService.getRideReviews(ride.getId())){
                dtos.add(reviewService.GenerateReviewDTO(review));
            }
            rideDetails.setReviews(dtos);
            return rideDetails;

        } catch (Exception e) {
            System.out.println("ERROR in createRideDetails for ride ID " +
                    (ride != null ? ride.getId() : "null") + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ActiveVehicle getActiveVehicleForRide(Long rideId){
        Ride ride = getRideById(rideId);
        return vehicleService.getActiveVehicle(ride.getDriver().getVehicle().getId());
    }


    public void finishRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow();
        if (!ride.getStatus().equals(RideStatus.IN_PROGRESS)) {
            throw new IllegalArgumentException("Ride with that id is not in progress");
        }

        Driver driver = ride.getDriver();
        Vehicle vehicle = driver.getVehicle();
        ActiveVehicle av = vehicleService.getActiveVehicle(vehicle.getId());

        ride.setActualEndLocation(av.getLocation());
        ride.setEndTime(LocalDateTime.now());
        ride.setStatus(RideStatus.FINISHED);
        rideRepository.save(ride);

        driver.setAvailable(true);
        driver.setCurrentlyWorking(true);
        driverRepository.save(driver);

        av.setRide(null);
        av.setAvailable(true);
        activeVehicleRepository.save(av);

        notificationService.rideFinishNotifyPassengers(ride);
    }

    @Transactional
    public RideResponse orderRide(@RequestBody RideRequest request,  @RequestHeader(value = "X-User-Email", required = false) String userEmail) {


        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalStateException("No user email provided");
        }

        Passenger loggedPassenger = userRepository.findByEmail(userEmail)
                .filter(u -> u instanceof Passenger)
                .map(u -> (Passenger) u)
                .orElseThrow(() -> new IllegalStateException("Logged user is not a passenger"));

        List<RideStatus> activeStatuses = List.of(RideStatus.REQUESTED, RideStatus.ACCEPTED, RideStatus.IN_PROGRESS);
        boolean hasActiveRide = rideRepository.existsByOrdererAndStatusIn(loggedPassenger, activeStatuses);
        if (hasActiveRide) {
            throw new IllegalStateException("Passenger already has an active ride");
        }

        LocalDateTime startTime = request.getStartTime() != null ? request.getStartTime() : LocalDateTime.now();


        if (startTime.isAfter(LocalDateTime.now().plusHours(5))) {
            throw new IllegalStateException("Cannot schedule a ride more than 5 hours in advance");
        }

        System.out.println("OVO SU INTERMEDIJARNI STOP POINTS KOJE JE KORISNIK POSLAO:");
        System.out.println(request.getIntermediateStops());

        List<Location> routePoints = new ArrayList<>();
        routePoints.add(request.getStartLocation());
        if (request.getIntermediateStops() != null) {
            for (Location loc : request.getIntermediateStops()) {
                if (loc == null) continue;

                if (loc.getLatitude() == null || loc.getLongitude() == null) continue;

                if (loc.getLatitude() == 0 && loc.getLongitude() == 0) continue;

                routePoints.add(loc);
            }
        }

        routePoints.add(request.getEndLocation());

        int totalDistance = 0;

        for (int i = 0; i < routePoints.size() - 1; i++) {

            Location from = routePoints.get(i);
            Location to = routePoints.get(i + 1);

            totalDistance += routeService
                    .calculateDistanceBetweenTwoPoints(from, to);
        }

        final double AVERAGE_SPEED_M_S = 11.11;

        long estimatedTimeSeconds =
                Math.max(1, Math.round(totalDistance / AVERAGE_SPEED_M_S));

        Driver driver = findAvailableDriverWithFullPriority(request.getVehicleType(),
                request.getStartLocation(), request.isBabyFriendly(), request.isPetFriendly(), startTime);

        if (driver == null) {
            throw new IllegalStateException("No available drivers at the moment");
        }


        Route route = new Route();
        route.setStartLocation(request.getStartLocation());
        route.setEndLocation(request.getEndLocation());

        List<RouteStop> stops = new ArrayList<>();
        if (request.getIntermediateStops() != null) {
            int order = 0;
            for (Location loc : request.getIntermediateStops()) {
                if (loc == null) continue;
                if (loc.getLatitude() == null || loc.getLongitude() == null) continue;
                if (loc.getLatitude() == 0 && loc.getLongitude() == 0) continue;

                RouteStop stop = new RouteStop();
                stop.setLocation(loc);
                stop.setStopOrder(order++);
                stop.setRoute(route);
                stops.add(stop);
            }
        }

        route.setStops(stops);

        route.setEstimatedDistanceMeters(totalDistance);
        route.setEstimatedDurationSeconds(estimatedTimeSeconds);

        routeRepository.save(route);

        Ride ride = new Ride();
        ride.setOrderer(loggedPassenger);
        ride.setDriver(driver);
        ride.setRoute(route);
        ride.setStartTime(
                request.getStartTime() != null ? request.getStartTime() : LocalDateTime.now()
        );
        ride.setBabyFriendly(request.isBabyFriendly());
        ride.setPetFriendly(request.isPetFriendly());
        ride.setStatus(RideStatus.REQUESTED);


        if (request.getLinkedPassengerEmails() != null && !request.getLinkedPassengerEmails().isEmpty()) {
            List<Passenger> linkedPassengers = passengerRepository.findAllByEmailIn(request.getLinkedPassengerEmails());
            ride.setLinkedPassengers(linkedPassengers);
        }


        ActiveVehicle activeVehicle = vehicleService.getActiveVehicle(driver.getVehicle().getId());
        vehicleService.setRouteForActiveVehicle(activeVehicle.getVehicleId(), routePoints);
        vehicleService.setLocation(activeVehicle.getVehicleId(), request.getStartLocation());
        activeVehicle.setAvailable(false);
        activeVehicle.setRide(ride);






        ActiveRideVehicleDetailsResponse tracking = vehicleService.getRideTrackingResponse(activeVehicle);
        LocalDateTime estimatedEndTime = ride.getStartTime().plusSeconds(tracking.getEstimatedTime());


        rideRepository.save(ride);

        RideResponse response = new RideResponse();
        response.setRideId(ride.getId());
        response.setEstimatedEndTime(estimatedEndTime);
        response.setVehicleId(activeVehicle.getVehicleId());
        response.setDriverName(driver.getFirstName() + " " + driver.getLastName());
        response.setRoutePoints(routePoints);
        response.setStatus(ride.getStatus());

        System.out.println("Ride ordered successfully with ID (this is in orderRide function): " + response);

        return response;
    }

    public Driver findAvailableDriverWithFullPriority(VehicleType vehicleType,
                                                      Location startLocation,
                                                      boolean babyFriendlyRequired,
                                                      boolean petFriendlyRequired,
                                                      LocalDateTime rideStartTime) {


        List<Driver> potentialDrivers = driverRepository.findAllByVehicle_Type(vehicleType);


        List<Driver> candidates = potentialDrivers.stream()
                .filter(d -> d.getWorkingHoursToday() < 8 && d.isAvailable())
                .filter(d -> {
                    ActiveVehicle av = vehicleService.getActiveVehicle(d.getVehicle().getId());

                    boolean vehicleOk = (!babyFriendlyRequired || av.getVehicle().isBabyFriendly())
                            && (!petFriendlyRequired || av.getVehicle().isPetFriendly());


                    boolean freeNow = av.isAvailable();
                    boolean freeSoon = false;

                    if (!freeNow && av.getRide() != null) {
                        ActiveRideVehicleDetailsResponse tracking = vehicleService.getRideTrackingResponse(av);
                        LocalDateTime rideEndTime = av.getRide().getStartTime().plusSeconds(tracking.getEstimatedTime());
                        long remainingSeconds = Duration.between(LocalDateTime.now(), rideEndTime).getSeconds();
                        freeSoon = remainingSeconds <= 600;
                    }

                    return vehicleOk && (freeNow || freeSoon);
                })
                .toList();

        if (candidates.isEmpty()) return null;


//        candidates.sort(Comparator.comparingDouble(d -> {
//            ActiveVehicle av = vehicleService.getActiveVehicle(d.getVehicle().getId());
//            System.out.println("Calculating distance for driver: " + d.getFirstName() + " " + d.getLastName());
//            System.out.println(av.getLocation());
//
//            return routeService.calculateDistanceBetweenTwoPoints(av.getLocation(), startLocation);
//        }));


        for (Driver d : candidates) {
            List<Ride> futureRides = getDriverRides(d.getId()).stream()
                    .filter(r -> r.getStartTime().isAfter(LocalDateTime.now()))
                    .toList();

            boolean conflict = futureRides.stream().anyMatch(r -> {
                ActiveVehicle av = vehicleService.getActiveVehicle(d.getVehicle().getId());
                ActiveRideVehicleDetailsResponse tracking = vehicleService.getRideTrackingResponse(av);
                LocalDateTime rideEndTime = av.getRide().getStartTime().plusSeconds(tracking.getEstimatedTime());
                return rideStartTime.isBefore(rideEndTime);
            });

            if (!conflict) {
                return d;
            }
        }

        return null;
    }

}
