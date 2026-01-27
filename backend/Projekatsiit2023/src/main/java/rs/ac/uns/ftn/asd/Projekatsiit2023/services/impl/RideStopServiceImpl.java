package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideStopRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideStopResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.ActiveVehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ActiveVehicleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideStopService;

import java.time.LocalDateTime;

@Service
public class RideStopServiceImpl implements RideStopService {

    private final RideRepository rideRepository;
    private final ActiveVehicleRepository activeVehicleRepository;
    private final DriverRepository driverRepository;

    public RideStopServiceImpl(RideRepository rideRepository,
                               ActiveVehicleRepository activeVehicleRepository,
                               DriverRepository driverRepository) {
        this.rideRepository = rideRepository;
        this.activeVehicleRepository = activeVehicleRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    @Transactional
    public RideStopResponse stopRide(Long rideId, RideStopRequest request) {
        // find ride by ID
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride with ID " + rideId + " not found"));

        // check if ride is in progress
        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new RuntimeException("Ride is not in progress. Only rides in progress can be stopped.");
        }

        // recalculate distance and price
        double originalDistance = ride.getRoute().getEstimatedDistanceMeters() / 1000.0;
        double newDistance = calculateNewDistance(ride, request.getLatitude(), request.getLongitude());
        double newPrice = recalculatePrice(ride, originalDistance, newDistance);

        // update ride details
        Location actualEnd = new Location();
        actualEnd.setLatitude(request.getLatitude());
        actualEnd.setLongitude(request.getLongitude());
        actualEnd.setAddress(request.getAddress());
        ride.setActualEndLocation(actualEnd);

        ride.setPrice(newPrice);
        ride.setStatus(RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());

        rideRepository.save(ride);

        // remove rideId from active vehicle
        ActiveVehicle activeVehicle = activeVehicleRepository.findById(ride.getDriver().getVehicle().getId()).orElseThrow();
        activeVehicle.setRide(null);
        activeVehicle.setAvailable(true);
        activeVehicleRepository.save(activeVehicle);

        // set driver available
        Driver driver = ride.getDriver();
        driver.setAvailable(true);
        driver.setCurrentlyWorking(false);
        driverRepository.save(driver);

        // return response
        return new RideStopResponse(
                true,
                "Ride #" + rideId + " stopped successfully at " + request.getAddress(),
                newPrice,
                newDistance,
                ride.getEndTime(),
                request.getAddress()
        );
    }

    @Override
    public double calculateNewDistance(Ride ride, double newLat, double newLng) {
        // FOR NOW: just return 90% of original distance
        double originalDistance = ride.getRoute().getEstimatedDistanceMeters() / 1000.0;
        return originalDistance * 0.9;
    }

    @Override
    public double recalculatePrice(Ride ride, double originalDistance, double newDistance) {
        // FOR NOW
        Double originalPrice = ride.getPrice();
        if (originalPrice == null) {
            originalPrice = 200.0;
        }

        double pricePerKm = originalPrice / originalDistance;
        return pricePerKm * newDistance;
    }

}
