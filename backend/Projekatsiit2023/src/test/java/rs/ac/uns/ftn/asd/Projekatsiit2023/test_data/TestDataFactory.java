package rs.ac.uns.ftn.asd.Projekatsiit2023.test_data;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TestDataFactory {
    public static Driver createDriver(Vehicle vehicle){
        Driver driver = new Driver();
        driver.setEmail("driver@example.com");
        driver.setPassword("securePassword123");
        driver.setFirstName("Petar");
        driver.setLastName("Petrovic");
        driver.setAddress("Bulevar Kralja Aleksandra 100, Beograd");
        driver.setPhone("+381641234567");
        driver.setRole(UserRole.DRIVER);
        driver.setAccountActivated(true);
        driver.setAccountBlocked(false);
        driver.setCreatedAt(LocalDateTime.now());

        driver.setVehicle(vehicle);
        return driver;
    }

    public static Vehicle createVehicle(){
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setPlateNumber("BG123AB");
        vehicle.setSeatNumber(5);
        vehicle.setBabyFriendly(true);
        vehicle.setPetFriendly(false);
        vehicle.setType(VehicleType.STANDARD);

        return vehicle;
    }

    public static Passenger createPassenger(){
        Passenger passenger = new Passenger();

        passenger.setEmail("passenger1@example.com");
        passenger.setPassword("securePassword123");
        passenger.setFirstName("Marko");
        passenger.setLastName("Markovic");
        passenger.setAddress("Bulevar Kralja Aleksandra 10, Beograd");
        passenger.setPhone("+381611234567");
        passenger.setRole(UserRole.PASSENGER);
        passenger.setAccountActivated(true);
        passenger.setAccountBlocked(false);
        passenger.setCreatedAt(LocalDateTime.now());
        passenger.setProfileImage(null);

        return passenger;
    }

    public static Route createRoute(){
        Route route = new Route();

        Location start = new Location();
        start.setLatitude(44.8176);
        start.setLongitude(20.4569);
        start.setAddress("Bulevar Kralja Aleksandra 1, Beograd");
        route.setStartLocation(start);

        Location end = new Location();
        end.setLatitude(44.8046);
        end.setLongitude(20.4781);
        end.setAddress("Nemanjina 10, Beograd");
        route.setEndLocation(end);

        route.setEstimatedDurationSeconds(900L);
        route.setEstimatedDistanceMeters(5000);

        return route;
    }

    public static ActiveVehicle createActiveVehicle(Vehicle vehicle, Ride ride){
        ActiveVehicle av = new ActiveVehicle();
        av.setRide(ride);
        av.setRouteIndex(0);
        av.setVehicle(vehicle);
        Location location = new Location();
        location.setLatitude(19.0);
        location.setLongitude(42.0);
        av.setLocation(location);
        return av;
    }

    public static Ride createRideInProgress(
            Driver driver,
            Passenger passenger,
            Route route
    ){
        Ride ride = new Ride();
        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setDriver(driver);
        ride.setOrderer(passenger);
        ride.setRoute(route);
        ride.setStartTime(LocalDateTime.now().minusMinutes(5));
        return ride;
    }

    public static Ride createRequestedRide(
            Driver driver,
            Passenger passenger,
            Route route
    ){
        Ride ride = new Ride();
        ride.setStatus(RideStatus.REQUESTED);
        ride.setDriver(driver);
        ride.setOrderer(passenger);
        ride.setRoute(route);
        ride.setStartTime(LocalDateTime.now().minusMinutes(5));
        return ride;
    }
}
