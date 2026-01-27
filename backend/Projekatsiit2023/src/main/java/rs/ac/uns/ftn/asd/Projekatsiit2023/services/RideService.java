package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.ReviewDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl.RouteServiceImpl;

import java.util.ArrayList;
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
            ActiveVehicleRepository activeVehicleRepository
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

    public void createMockRides() {

        // --- RUTA 1 ---
        Route r1 = new Route();
        r1.setStartLocation(new Location(
                45.2515, 19.8369, "Bulevar oslobođenja, Novi Sad"
        ));
        r1.setEndLocation(new Location(
                45.2580, 19.8447, "Spens, Novi Sad"
        ));
        r1.setEstimatedDistanceMeters(2200);
        r1.setEstimatedDurationSeconds(500L);

        routeRepository.save(r1);

        Ride ride1 = new Ride();
        ride1.setOrderer(passengerRepository.findById(15L).orElseThrow());
        ride1.setDriver(driverRepository.findById(25L).orElseThrow());
        ride1.setRoute(r1);
        ride1.setStartTime(LocalDateTime.now().minusMinutes(5));
        ride1.setBabyFriendly(false);
        ride1.setPetFriendly(true);
        ride1.setStatus(RideStatus.IN_PROGRESS);

        rideRepository.save(ride1);

        String route1 = routeService.calculateRouteThroughPoints(
                routeService.convertRouteToLocationList(r1)
        );
        vehicleService.addActiveVehicle(
                ride1.getDriver().getVehicle(),
                r1.getStartLocation(),
                route1,
                false,
                ride1
        );

        // --- RUTA 2 ---
        Route r2 = new Route();
        r2.setStartLocation(new Location(
                45.2450, 19.8230, "Telep, Novi Sad"
        ));
        r2.setEndLocation(new Location(
                45.2596, 19.8332, "Železnička stanica, Novi Sad"
        ));
        r2.setEstimatedDistanceMeters(3700);
        r2.setEstimatedDurationSeconds(800L);

        routeRepository.save(r2);

        Ride ride2 = new Ride();
        ride2.setOrderer(passengerRepository.findById(17L).orElseThrow());
        ride2.setDriver(driverRepository.findById(26L).orElseThrow());
        ride2.setRoute(r2);
        ride2.setStartTime(LocalDateTime.now().minusMinutes(3));
        ride2.setBabyFriendly(true);
        ride2.setPetFriendly(false);
        ride2.setStatus(RideStatus.IN_PROGRESS);

        rideRepository.save(ride2);

        String route2 = routeService.calculateRouteThroughPoints(
                routeService.convertRouteToLocationList(r2)
        );
        vehicleService.addActiveVehicle(
                ride2.getDriver().getVehicle(),
                r2.getStartLocation(),
                route2,
                false,
                ride2
        );

        // --- RUTA 3 ---
        Route r3 = new Route();
        r3.setStartLocation(new Location(
                45.2623, 19.8424, "Klisa, Novi Sad"
        ));
        r3.setEndLocation(new Location(
                45.2558, 19.8443, "Trg slobode, Novi Sad"
        ));
        r3.setEstimatedDistanceMeters(4800);
        r3.setEstimatedDurationSeconds(1000L);

        routeRepository.save(r3);

        Ride ride3 = new Ride();
        ride3.setOrderer(passengerRepository.findById(18L).orElseThrow());
        ride3.setDriver(driverRepository.findById(27L).orElseThrow());
        ride3.setRoute(r3);
        ride3.setStartTime(LocalDateTime.now().minusMinutes(7));
        ride3.setBabyFriendly(false);
        ride3.setPetFriendly(false);
        ride3.setStatus(RideStatus.IN_PROGRESS);

        rideRepository.save(ride3);

        String route3 = routeService.calculateRouteThroughPoints(
                routeService.convertRouteToLocationList(r3)
        );
        vehicleService.addActiveVehicle(
                ride3.getDriver().getVehicle(),
                r3.getStartLocation(),
                route3,
                false,
                ride3
        );

        // --- RUTA 4 ---
        Route r4 = new Route();
        r4.setStartLocation(new Location(
                45.2487, 19.8571, "Liman III, Novi Sad"
        ));
        r4.setEndLocation(new Location(
                45.2539, 19.8712, "Petrovaradinska tvrđava, Novi Sad"
        ));
        r4.setEstimatedDistanceMeters(3100);
        r4.setEstimatedDurationSeconds(700L);

        routeRepository.save(r4);

        Ride ride4 = new Ride();
        ride4.setOrderer(passengerRepository.findById(19L).orElseThrow());
        ride4.setDriver(driverRepository.findById(28L).orElseThrow());
        ride4.setRoute(r4);
        ride4.setStartTime(LocalDateTime.now().minusMinutes(2));
        ride4.setBabyFriendly(true);
        ride4.setPetFriendly(true);
        ride4.setStatus(RideStatus.IN_PROGRESS);

        rideRepository.save(ride4);

        String route4 = routeService.calculateRouteThroughPoints(
                routeService.convertRouteToLocationList(r4)
        );
        vehicleService.addActiveVehicle(
                ride4.getDriver().getVehicle(),
                r4.getStartLocation(),
                route4,
                false,
                ride4
        );
    }


    public void finishRide(Long rideId){
        Ride ride = rideRepository.findById(rideId).orElseThrow();
        if (!ride.getStatus().equals(RideStatus.IN_PROGRESS)){
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
    }

}
