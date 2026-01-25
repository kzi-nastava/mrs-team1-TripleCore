package rs.ac.uns.ftn.asd.Projekatsiit2023.services.driving;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.ActiveVehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ActiveVehicleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideService;

import java.util.List;
import java.util.Optional;

@Service
public class DrivingSimulationService {
    private final ActiveVehicleRepository activeVehicleRepository;

    public  DrivingSimulationService(
            ActiveVehicleRepository activeVehicleRepository
    ){
        this.activeVehicleRepository = activeVehicleRepository;
    }

    public static Location moveNorthBy10Meters(Location location) {
        final double METERS_PER_DEGREE_LATITUDE = 111_320.0;
        final double DELTA_LAT = 10.0 / METERS_PER_DEGREE_LATITUDE;

        Location newLocation = new Location();
        newLocation.setLatitude(location.getLatitude() + DELTA_LAT);
        newLocation.setLongitude(location.getLongitude());

        return newLocation;
    }

    public void MoveVehicle(Long vehicleId){
        Optional<ActiveVehicle> avOpt = activeVehicleRepository.findById(vehicleId);

        if (avOpt.isEmpty()){
            throw new EntityNotFoundException("Active vehicle not found");
        }

        ActiveVehicle av = avOpt.get();
        Location newLocation = moveNorthBy10Meters(av.getLocation());
        av.setLocation(newLocation);

        activeVehicleRepository.save(av);
    }
}
