package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.VehicleRepository;

import java.time.LocalDateTime;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public DriverService(DriverRepository dr, VehicleRepository vr){
        this.driverRepository = dr;
        this.vehicleRepository = vr;
    }

    public Driver createDriverWithVehicle() {
        // 1. Napravi vozilo
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Prius");
        vehicle.setPlateNumber("BG123AB");
        vehicle.setSeatNumber(4);
        vehicle.setBabyFriendly(true);
        vehicle.setPetFriendly(false);
        vehicle.setType(VehicleType.VAN);

        // Snimi vozilo prvo da dobije ID
        vehicle = vehicleRepository.save(vehicle);

        // 2. Napravi vozača
        Driver driver = new Driver();
        driver.setEmail("driver@example.com");
        driver.setPassword("password"); // kasnije hash-uj
        driver.setFirstName("Marko");
        driver.setLastName("Marković");
        driver.setAddress("Beograd");
        driver.setPhone("0601234567");
        driver.setRole(UserRole.DRIVER);
        driver.setAccountActivated(true);
        driver.setAccountBlocked(false);
        driver.setCreatedAt(LocalDateTime.now());
        driver.setCurrentlyWorking(false);
        driver.setWorkingHoursToday(0);
        driver.setAvailable(true);

        // Poveži vozilo
        driver.setVehicle(vehicle);

        // 3. Snimi vozača
        driver = driverRepository.save(driver);

        return driver;
    }

    public Driver getDriverById(Long id){
        return driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Driver with id: " + id + " not found"));
    }

}
