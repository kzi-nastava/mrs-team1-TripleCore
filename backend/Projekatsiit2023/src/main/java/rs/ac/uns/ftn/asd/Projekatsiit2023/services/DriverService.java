package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle.ActiveRideVehicleDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.VehicleRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    private final RideService rideService;

    public DriverService(
            DriverRepository dr,
            VehicleRepository vr,
            RideService rs) {
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
                details.add(rideService.createRideDetails(ride));
            }
            return details;
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void AddMockDrivers(){
        Vehicle v1 = new Vehicle();
        v1.setBrand("Hyundai");
        v1.setModel("Elantra");
        v1.setPlateNumber("NS-901-AA");
        v1.setSeatNumber(4);
        v1.setBabyFriendly(false);
        v1.setPetFriendly(true);
        v1.setType(VehicleType.STANDARD);

        vehicleRepository.save(v1);

        Driver d1 = new Driver();
        d1.setEmail("slavko@example.com");
        d1.setPassword("slavko123");
        d1.setFirstName("Slavko");
        d1.setLastName("Slavkovic");
        d1.setAddress("Milene Dravic 5");
        d1.setPhone("123456789");
        d1.setCreatedAt(LocalDateTime.now().minusHours(20));
        d1.setAccountActivated(true);
        d1.setAccountBlocked(false);
        d1.setCurrentlyWorking(true);
        d1.setWorkingHoursToday(2.0);
        d1.setLastWorkStart(LocalDateTime.now().minusHours(2));
        d1.setAvailable(true);
        d1.setVehicle(v1);

        driverRepository.save(d1);

        Vehicle v2 = new Vehicle();
        v2.setBrand("Peugeot");
        v2.setModel("308");
        v2.setPlateNumber("NS-456-BB");
        v2.setSeatNumber(4);
        v2.setBabyFriendly(true);
        v2.setPetFriendly(false);
        v2.setType(VehicleType.STANDARD);

        vehicleRepository.save(v2);

        Driver d2 = new Driver();
        d2.setEmail("sanja@example.com");
        d2.setPassword("sanja123");
        d2.setFirstName("Sanja");
        d2.setLastName("Savic");
        d2.setAddress("Milene Dravic 6");
        d2.setPhone("123456787");
        d2.setCreatedAt(LocalDateTime.now().minusHours(20));
        d2.setAccountActivated(true);
        d2.setAccountBlocked(false);
        d2.setCurrentlyWorking(false);
        d2.setWorkingHoursToday(5.0);
        d2.setLastWorkStart(LocalDateTime.now().minusHours(8));
        d2.setAvailable(true);
        d2.setVehicle(v2);

        driverRepository.save(d2);

        Vehicle v3 = new Vehicle();
        v3.setBrand("Ford");
        v3.setModel("Kuga");
        v3.setPlateNumber("NS-789-CC");
        v3.setSeatNumber(5);
        v3.setBabyFriendly(true);
        v3.setPetFriendly(true);
        v3.setType(VehicleType.STANDARD);

        vehicleRepository.save(v3);

        Driver d3 = new Driver();
        d3.setEmail("goran@example.com");
        d3.setPassword("goran123");
        d3.setFirstName("Goran");
        d3.setLastName("Goranovic");
        d3.setAddress("Milene Dravic 7");
        d3.setPhone("123456756");
        d3.setCreatedAt(LocalDateTime.now().minusHours(20));
        d3.setAccountActivated(true);
        d3.setAccountBlocked(false);
        d3.setCurrentlyWorking(true);
        d3.setWorkingHoursToday(6.0);
        d3.setLastWorkStart(LocalDateTime.now().minusHours(6));
        d3.setAvailable(true);
        d3.setVehicle(v3);

        driverRepository.save(d3);

        Vehicle v4 = new Vehicle();
        v4.setBrand("Renault");
        v4.setModel("Trafic");
        v4.setPlateNumber("NS-321-DD");
        v4.setSeatNumber(8);
        v4.setBabyFriendly(false);
        v4.setPetFriendly(false);
        v4.setType(VehicleType.VAN);

        vehicleRepository.save(v4);

        Driver d4 = new Driver();
        d4.setEmail("mia@example.com");
        d4.setPassword("mia123");
        d4.setFirstName("Mia");
        d4.setLastName("Mijic");
        d4.setAddress("Milene Dravic 10");
        d4.setPhone("123456777");
        d4.setCreatedAt(LocalDateTime.now().minusHours(20));
        d4.setAccountActivated(true);
        d4.setAccountBlocked(false);
        d4.setCurrentlyWorking(false);
        d4.setWorkingHoursToday(10.0);
        d4.setLastWorkStart(LocalDateTime.now().minusHours(14));
        d4.setAvailable(false);
        d4.setVehicle(v4);

        driverRepository.save(d4);

    }
}
