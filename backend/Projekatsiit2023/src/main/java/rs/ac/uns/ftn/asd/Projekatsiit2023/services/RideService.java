package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.PassengerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RouteRepository;

import java.time.LocalDateTime;
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

    public Ride createTestRide() {

        Driver driver = driverRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Passenger orderer = passengerRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        Route route = routeRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        Ride ride = new Ride();
        ride.setDriver(driver);
        ride.setOrderer(orderer);
        ride.setRoute(route);

        ride.setStartTime(LocalDateTime.now());
        ride.setStatus(RideStatus.REQUESTED);
        ride.setBabyFriendly(false);
        ride.setPetFriendly(false);
        ride.setPrice(850.0);

        return rideRepository.save(ride);
    }

    public Ride getRideById(Long id){
        return rideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ride with id: " + id + " not found"));
    }
}
