package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.PassengerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RouteRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final RouteRepository routeRepository;

    public RideService(
            RideRepository rideRepository,
            DriverRepository driverRepository,
            PassengerRepository passengerRepository,
            RouteRepository routeRepository
    ) {
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
        this.passengerRepository = passengerRepository;
        this.routeRepository = routeRepository;
    }

    public Ride getRideById(Long id){
        return rideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ride with id: " + id + " not found"));
    }

    public List<Ride> getDriverRides(Long driverId){
        try{
            return rideRepository.findByDriverId(driverId);
        } catch (Exception e){
            return new ArrayList<>();
        }
    }

    public static RideDetailsResponse createRideDetails(Ride ride) {
        RideDetailsResponse rideDetails = new RideDetailsResponse();

        // Passengers
        rideDetails.setOrdererName(
                ride.getOrderer().getFirstName() + " " + ride.getOrderer().getLastName()
        );

        rideDetails.setLinkedPassengers(
                ride.getLinkedPassengers()
                        .stream()
                        .map(p -> p.getFirstName() + " " + p.getLastName())
                        .toList()
        );

        // Driver
        rideDetails.setDriverName(
                ride.getDriver().getFirstName() + " " + ride.getDriver().getLastName()
        );
        rideDetails.setVehicle(
                ride.getDriver().getVehicle().getBrand() + " " + ride.getDriver().getVehicle().getModel()
        );

        // Route
        rideDetails.setStartLocation(ride.getRoute().getStartLocation());
        rideDetails.setEndLocation(ride.getRoute().getEndLocation());
        rideDetails.setRouteStops(
                ride.getRoute().getStops()
                        .stream()
                        .map(RouteStop::getLocation)
                        .toList()
        );

        // Time
        rideDetails.setStartTime(ride.getStartTime());
        rideDetails.setEndTime(ride.getEndTime());

        // Panic
        rideDetails.setPanic(ride.isPanic());
        rideDetails.setPanicTriggeredBy(
                ride.getPanicTriggeredBy() != null
                        ? ride.getPanicTriggeredBy().getFirstName() + " " + ride.getPanicTriggeredBy().getLastName()
                        : null
        );
        rideDetails.setPanicTriggeredAt(ride.getPanicTriggeredAt());

        // Other info
        rideDetails.setPrice(ride.getPrice());
        rideDetails.setStatus(ride.getStatus());
        rideDetails.setCancelledBy(ride.getCancelledBy() != null ? ride.getCancelledBy().getRole() : null);
        rideDetails.setReviews(new ArrayList<>());
        rideDetails.setInconsistencies(ride.getInconsistencies());

        return rideDetails;
    }

}
