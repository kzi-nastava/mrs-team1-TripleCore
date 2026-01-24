package rs.ac.uns.ftn.asd.Projekatsiit2023.services.driving;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.ActiveVehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ActiveVehicleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideService;

@Service
public class DrivingSimulationService {
    private final RideService rideService;
    private final ActiveVehicleRepository activeVehicleRepository;

    public  DrivingSimulationService(
            RideService rideService,
            ActiveVehicleRepository activeVehicleRepository
    ){
        this.rideService = rideService;
        this.activeVehicleRepository = activeVehicleRepository;
    }
}
