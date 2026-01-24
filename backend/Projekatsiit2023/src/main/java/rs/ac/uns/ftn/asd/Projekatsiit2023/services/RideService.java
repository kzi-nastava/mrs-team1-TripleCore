package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.ReviewDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.*;

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

    public RideService(
            RideRepository rideRepository,
            DriverRepository driverRepository,
            PassengerRepository passengerRepository,
            RouteRepository routeRepository,
            ReviewService reviewService,
            UserRepository userRepository,
            PanicService panicService,
            VehicleService vehicleService
    ) {
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
        this.passengerRepository = passengerRepository;
        this.routeRepository = routeRepository;
        this.reviewService = reviewService;
        this.userRepository = userRepository;
        this.panicService = panicService;
        this.vehicleService = vehicleService;
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

}
