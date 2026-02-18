package rs.ac.uns.ftn.asd.Projekatsiit2023.services.driving;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Transactional
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

    @Transactional
    public void moveActiveRideVehicle(Long vehicleId){
        ActiveVehicle av = activeVehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Active vehicle not found"));
        try{
            Location loc = vehicleService.getLocationAtRouteIndex(av.getRouteCoordinates(), av.getRouteIndex() + 1);

            av.setRouteIndex(av.getRouteIndex() + 1);
            av.setLocation(loc);
            activeVehicleRepository.saveAndFlush(av);
        } catch (IndexOutOfBoundsException ex){
            activeVehicleRepository.saveAndFlush(av);
        }
    }

    @Transactional
    public void moveAllBusyVehicles(){
        List<ActiveVehicle> avs = activeVehicleRepository.findByRideIdIsNotNull();
        for (ActiveVehicle av : avs){
            try{
                Location loc = vehicleService.getLocationAtRouteIndex(av.getRouteCoordinates(), av.getRouteIndex() + 1);

                av.setRouteIndex(av.getRouteIndex() + 1);
                av.setLocation(loc);
                activeVehicleRepository.save(av);
            } catch (IndexOutOfBoundsException ex){
                // When the vehicle reaches the end of the ride route it should stop
                // So that when the driver finishes the ride the car is not on a random location
            }
        }


    }


}
