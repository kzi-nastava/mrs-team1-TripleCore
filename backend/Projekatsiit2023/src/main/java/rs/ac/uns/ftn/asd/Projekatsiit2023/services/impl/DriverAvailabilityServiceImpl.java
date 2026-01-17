package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.DriverAvailabilityService;

@Service
@RequiredArgsConstructor
public class DriverAvailabilityServiceImpl implements DriverAvailabilityService {

    private final DriverRepository driverRepository;
    private final RideRepository rideRepository;

    @Override
    @Transactional
    public String changeAvailability(Long driverId, boolean available) {
        // find the driver
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));

        // check if setting to unavailable while having active ride
        if (!available) {
            boolean hasActiveRide = hasActiveRide(driverId);

            if (hasActiveRide) {
                // If driver has an active ride, do not change status
                // later: add a flag to mark pending unavailability
                return "You have an active ride. You will become unavailable AFTER the ride finishes. "
                        + "System will not offer you to new passengers until you become available again.";
            }
        }

        // change status
        driver.setAvailable(available);
        driverRepository.save(driver);

        String status = available ? "available" : "unavailable";
        return "Driver is now " + status + " for new rides.";
    }

    @Override
    public boolean isDriverAvailable(Long driverId) {
        return driverRepository.findById(driverId)
                .map(Driver::isAvailable)
                .orElse(false);
    }

    @Override
    public boolean hasActiveRide(Long driverId) {
        return rideRepository.existsByDriverIdAndStatus(driverId, RideStatus.IN_PROGRESS);
    }
}