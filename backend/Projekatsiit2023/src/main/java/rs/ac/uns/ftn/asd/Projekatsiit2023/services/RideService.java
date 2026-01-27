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
            RouteServiceImpl routeService
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
                45.2459, 19.8514, "Liman IV, Novi Sad"
        ));
        r1.setEndLocation(new Location(
                45.2539, 19.8712, "Petrovaradinska tvrđava, Novi Sad"
        ));
        r1.setEstimatedDistanceMeters(4100);
        r1.setEstimatedDurationSeconds(900L);

        routeRepository.save(r1);

        Ride ride1 = new Ride();
        ride1.setOrderer(passengerRepository.findById(2L).orElseThrow());
        ride1.setDriver(driverRepository.findById(1L).orElseThrow());
        ride1.setRoute(r1);
        ride1.setStartTime(LocalDateTime.now().plusMinutes(10));
        ride1.setBabyFriendly(true);
        ride1.setPetFriendly(false);
        ride1.setStatus(RideStatus.IN_PROGRESS);

        rideRepository.save(ride1);
        String route1 = routeService.calculateRouteThroughPoints(routeService.convertRouteToLocationList(r1));
        vehicleService.addActiveVehicle(ride1.getDriver().getVehicle(), ride1.getRoute().getStartLocation(), route1, false, ride1);

        // --- RUTA 2 ---
        Route r2 = new Route();
        r2.setStartLocation(new Location(
                45.2620, 19.8105, "Detelinara, Novi Sad"
        ));
        r2.setEndLocation(new Location(
                45.2539, 19.8712, "Petrovaradinska tvrđava, Novi Sad"
        ));
        r2.setEstimatedDistanceMeters(4500);
        r2.setEstimatedDurationSeconds(950L);

        routeRepository.save(r2);

        Ride ride2 = new Ride();
        ride2.setOrderer(passengerRepository.findById(15L).orElseThrow());
        ride2.setDriver(driverRepository.findById(20L).orElseThrow());
        ride2.setRoute(r2);
        ride2.setStartTime(LocalDateTime.now().plusMinutes(15));
        ride2.setBabyFriendly(false);
        ride2.setPetFriendly(true);
        ride2.setStatus(RideStatus.IN_PROGRESS);

        rideRepository.save(ride2);
        String route2 = routeService.calculateRouteThroughPoints(routeService.convertRouteToLocationList(r2));
        vehicleService.addActiveVehicle(ride2.getDriver().getVehicle(), ride2.getRoute().getStartLocation(), route2, false, ride2);


        // --- RUTA 3 ---
        Route r3 = new Route();
        r3.setStartLocation(new Location(
                45.2420, 19.8150, "Telep, Novi Sad"
        ));
        r3.setEndLocation(new Location(
                45.2558, 19.8443, "Trg slobode, Novi Sad"
        ));
        r3.setEstimatedDistanceMeters(3600);
        r3.setEstimatedDurationSeconds(850L);

        routeRepository.save(r3);

        Ride ride3 = new Ride();
        ride3.setOrderer(passengerRepository.findById(17L).orElseThrow());
        ride3.setDriver(driverRepository.findById(21L).orElseThrow());
        ride3.setRoute(r3);
        ride3.setStartTime(LocalDateTime.now().plusMinutes(20));
        ride3.setBabyFriendly(true);
        ride3.setPetFriendly(true);
        ride3.setStatus(RideStatus.IN_PROGRESS);


        rideRepository.save(ride3);
        String route3 = routeService.calculateRouteThroughPoints(routeService.convertRouteToLocationList(r3));
        vehicleService.addActiveVehicle(ride3.getDriver().getVehicle(), ride3.getRoute().getStartLocation(), route3, false, ride3);

        // --- RUTA 4 ---
        Route r4 = new Route();
        r4.setStartLocation(new Location(
                45.2469, 19.8517, "Liman I, Novi Sad"
        ));
        r4.setEndLocation(new Location(
                45.2455, 19.8598, "Liman IV, Novi Sad"
        ));
        r4.setEstimatedDistanceMeters(1500);
        r4.setEstimatedDurationSeconds(400L);

        routeRepository.save(r4);

        Ride ride4 = new Ride();
        ride4.setOrderer(passengerRepository.findById(18L).orElseThrow());
        ride4.setDriver(driverRepository.findById(22L).orElseThrow());
        ride4.setRoute(r4);
        ride4.setStartTime(LocalDateTime.now().plusMinutes(25));
        ride4.setBabyFriendly(false);
        ride4.setPetFriendly(false);
        ride4.setStatus(RideStatus.IN_PROGRESS);

        rideRepository.save(ride4);
        String route4 = routeService.calculateRouteThroughPoints(routeService.convertRouteToLocationList(r4));
        vehicleService.addActiveVehicle(ride4.getDriver().getVehicle(), ride4.getRoute().getStartLocation(), route4, false, ride4);

        // --- RUTA 5 ---
        Route r5 = new Route();
        r5.setStartLocation(new Location(
                45.2420, 19.8150, "Telep, Novi Sad"
        ));
        r5.setEndLocation(new Location(
                45.2469, 19.8517, "Liman I, Novi Sad"
        ));
        r5.setEstimatedDistanceMeters(3500);
        r5.setEstimatedDurationSeconds(850L);

        routeRepository.save(r5);

        Ride ride5 = new Ride();
        ride5.setOrderer(passengerRepository.findById(19L).orElseThrow());
        ride5.setDriver(driverRepository.findById(23L).orElseThrow());
        ride5.setRoute(r5);
        ride5.setStartTime(LocalDateTime.now().plusMinutes(30));
        ride5.setBabyFriendly(true);
        ride5.setPetFriendly(false);
        ride5.setStatus(RideStatus.IN_PROGRESS);

        rideRepository.save(ride5);
        String route5 = routeService.calculateRouteThroughPoints(routeService.convertRouteToLocationList(r5));
        vehicleService.addActiveVehicle(ride5.getDriver().getVehicle(), ride5.getRoute().getStartLocation(), route5, false, ride5);
    }

}
