package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

public interface DriverAvailabilityService {

    String changeAvailability(Long driverId, boolean available);
    boolean isDriverAvailable(Long driverId);
    boolean hasActiveRide(Long driverId);
}