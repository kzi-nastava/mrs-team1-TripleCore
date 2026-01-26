package rs.ac.uns.ftn.asd.Projekatsiit2023.services.driving;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.ActiveVehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ActiveVehicleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.VehicleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.VehicleService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl.RouteServiceImpl;

import java.util.List;
import java.util.Optional;

@Service
public class DrivingSimulationService {
    private final ActiveVehicleRepository activeVehicleRepository;
    private final VehicleService vehicleService;
    private final RouteServiceImpl routeService;

    public  DrivingSimulationService(
            ActiveVehicleRepository activeVehicleRepository,
            VehicleService vehicleService,
            RouteServiceImpl routeService
    ){
        this.activeVehicleRepository = activeVehicleRepository;
        this.vehicleService = vehicleService;
        this.routeService = routeService;
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

    public void moveAllIdleVehicles(){
        // all active vehicles not currently driving a scheduled ride
        List<ActiveVehicle> avs = activeVehicleRepository.findByRideIdIsNull();

        for (ActiveVehicle av : avs){
            try{
                Location loc = vehicleService.getLocationAtRouteIndex(av.getRouteCoordinates(), av.getRouteIndex() + 1);

                av.setRouteIndex(av.getRouteIndex() + 1);
                av.setLocation(loc);
                activeVehicleRepository.save(av);
            } catch (IndexOutOfBoundsException ex){
                Location newDestination = routeService.getRandomNoviSadLocation();
                String newRoute = routeService.calculateRouteThroughPoints(
                        List.of(av.getLocation(), newDestination));

                av.setRouteCoordinates(newRoute);
                av.setRouteIndex(0);
                activeVehicleRepository.save(av);
            }
        }
    }
}
