package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle.ActiveRideVehicleDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle.ActiveVehicleLocationResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.ActiveVehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ActiveVehicleRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.driving.DrivingSimulationService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl.RouteServiceImpl;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final ActiveVehicleRepository activeVehicleRepository;
    private final DrivingSimulationService drivingSimulationService;
    private final RouteServiceImpl routeService;

    public VehicleService(
            ActiveVehicleRepository activeVehicleRepository,
            DrivingSimulationService drivingSimulationService,
            RouteServiceImpl routeService){
        this.activeVehicleRepository = activeVehicleRepository;
        this.drivingSimulationService = drivingSimulationService;
        this.routeService = routeService;
    }

    public List<ActiveVehicle> getActiveVehicles() {
        return activeVehicleRepository.findAll();
    }

    public ActiveVehicle getActiveVehicle(Long vehicleId){
        Optional<ActiveVehicle> avOpt = activeVehicleRepository.findById(vehicleId);

        if (avOpt.isEmpty()){
            throw new EntityNotFoundException("Active vehicle not found");
        }
        return avOpt.get();
    }


    public void addActiveVehicle(Vehicle vehicle, Location location, boolean available) {

        if (activeVehicleRepository.existsById(vehicle.getId())) {
            throw new IllegalStateException("Vehicle is already active");
        }

        ActiveVehicle activeVehicle = new ActiveVehicle(vehicle, location, available);
        activeVehicleRepository.save(activeVehicle);
    }

    @Transactional
    public void setAvailability(Long vehicleId, boolean available) {
        ActiveVehicle av = activeVehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Active vehicle not found"));

        av.setAvailable(available);
    }

    @Transactional
    public void setLocation(Long vehicleId, Location newLocation){
        ActiveVehicle av = activeVehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Active vehicle not found"));

        av.setLocation(newLocation);
    }

    @Transactional
    public void deactivateVehicle(Long vehicleId) {

        if (!activeVehicleRepository.existsById(vehicleId)) {
            throw new EntityNotFoundException(
                    "Active vehicle with id " + vehicleId + " not found");
        }

        activeVehicleRepository.deleteById(vehicleId);
    }

    public List<ActiveVehicleLocationResponse> getActiveVehicleLocations(){
        List<ActiveVehicle> activeVehicles = getActiveVehicles();
        return activeVehicles
                .stream()
                .map(av -> new ActiveVehicleLocationResponse(
                        av.getLocation().getLatitude(),
                        av.getLocation().getLongitude(),
                        av.isAvailable()
                )).toList();
    }

    public ActiveRideVehicleDetailsResponse generateActiveRideVehicleDeatils(ActiveVehicle av){
        /*
        * There need to be checks weather or not the ride is actually in progress
        * and if the ride is in progress there needs to be information about it in the active vehicle*/

        ActiveRideVehicleDetailsResponse response =
                new ActiveRideVehicleDetailsResponse();

        Ride ride = av.getRide();
        if (ride != null){
            response.setRideId(ride.getId());
        }
        response.setVehicleId(av.getVehicleId());

        Location location = new Location();
        location.setLatitude(av.getLocation().getLatitude());
        location.setLongitude(av.getLocation().getLongitude());

        response.setVehicleLocation(location);

        // hardcoded for now
        response.setEstimatedTime(900L);
        response.setEstimatedDistance(3200);

        return response;
    }

    public void moveVehicleTest(Long vehicleId){
        drivingSimulationService.MoveVehicle(vehicleId);
    }

    public void setRouteForActiveVehicle(Long vehicleId, List<Location> points){
            ActiveVehicle av = activeVehicleRepository.findById(vehicleId)
                    .orElseThrow();
            String route = routeService.calculateRouteThroughPoints(points);
            av.setRouteCoordinates(route);
            av.setRouteIndex(0);
            activeVehicleRepository.save(av);
    }

    public Location getLocationAtRouteIndex(String routeCoordinates, int routeIndex) {
        if (routeCoordinates == null || routeCoordinates.isBlank()) {
            throw new IllegalArgumentException("Route coordinates are empty");
        }

        String[] points = routeCoordinates.split(";");

        if (routeIndex < 0 || routeIndex >= points.length) {
            throw new IndexOutOfBoundsException(
                    "Route index " + routeIndex + " is out of bounds (0-" + (points.length - 1) + ")"
            );
        }

        String point = points[routeIndex];
        String[] latLon = point.split(",");

        if (latLon.length != 2) {
            throw new IllegalStateException("Invalid coordinate format: " + point);
        }

        double latitude = Double.parseDouble(latLon[0]);
        double longitude = Double.parseDouble(latLon[1]);

        return new Location(latitude, longitude, null);
    }


    public void moveActiveVehicle(Long vehicleId){
        ActiveVehicle av = activeVehicleRepository.findById(vehicleId).orElseThrow();
        Location loc = getLocationAtRouteIndex(av.getRouteCoordinates(), av.getRouteIndex() + 1);

        av.setRouteIndex(av.getRouteIndex() + 1);
        av.setLocation(loc);
        activeVehicleRepository.save(av);
    }
}
