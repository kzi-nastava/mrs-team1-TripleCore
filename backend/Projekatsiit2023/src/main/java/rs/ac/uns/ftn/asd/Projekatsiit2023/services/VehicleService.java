package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle.ActiveRideVehicleDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle.ActiveVehicleLocationResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.ActiveVehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.ActiveVehicleRepository;
import java.util.List;

@Service
public class VehicleService {

    private final ActiveVehicleRepository activeVehicleRepository;

    public VehicleService(ActiveVehicleRepository activeVehicleRepository){
        this.activeVehicleRepository = activeVehicleRepository;
    }

    public List<ActiveVehicle> getActiveVehicles() {
        return activeVehicleRepository.findAll();
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
}
