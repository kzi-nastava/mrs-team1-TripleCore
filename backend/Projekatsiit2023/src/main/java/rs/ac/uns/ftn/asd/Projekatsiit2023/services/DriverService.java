package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.VehicleRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    private final RideService rideService;

    public DriverService(
            DriverRepository dr,
            VehicleRepository vr,
            RideService rs){
        this.driverRepository = dr;
        this.vehicleRepository = vr;
        this.rideService = rs;
    }

    public Driver getDriverById(Long id){
        return driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Driver with id: " + id + " not found"));
    }

    // change here so the function throws exceptions
    public List<RideDetailsResponse> getRideHistory(Long driverId){
        try{
            List<RideDetailsResponse> details = new ArrayList<>();
            for (Ride ride : rideService.getDriverRides(driverId)){
                if (ride.getStatus().equals(RideStatus.CANCELLED) || ride.getStatus().equals(RideStatus.FINISHED)){
                    details.add(rideService.createRideDetails(ride));
                }

            }
            return details;
        } catch (Exception e){
            return new ArrayList<>();
        }
    }
}
