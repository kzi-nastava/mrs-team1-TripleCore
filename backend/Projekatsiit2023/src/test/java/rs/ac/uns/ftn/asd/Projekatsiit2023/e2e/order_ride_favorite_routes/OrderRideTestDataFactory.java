package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.order_ride_favorite_routes;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;

import java.time.LocalDateTime;

public class OrderRideTestDataFactory {

    public static Passenger createTestPassenger() {
        Passenger p = new Passenger();

        p.setEmail("testpassenger@example.com");
        p.setPassword("passwordPassenger");
        p.setFirstName("Test");
        p.setLastName("Passenger");
        p.setAddress("Test Address");
        p.setPhone("+381600000000");
        p.setRole(UserRole.PASSENGER);
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now());

        return p;
    }

    public static Route createTestRoute() {
        Route route = new Route();

        Location start = new Location();
        start.setLatitude(45.2671);
        start.setLongitude(19.8335);
        start.setAddress("ĆУлица Кисачка 82, Нови Сад, 21101 Нови Сад Нови Сад Србија");

        Location end = new Location();
        end.setLatitude(45.2550);
        end.setLongitude(19.8450);
        end.setAddress("Улица Краља Александра, Нови Сад, 21101 Нови Сад Нови Сад Србија");

        route.setStartLocation(start);
        route.setEndLocation(end);
        route.setEstimatedDurationSeconds(600L);
        route.setEstimatedDistanceMeters(3000);

        return route;
    }

    public static Vehicle createTestVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.STANDARD);
        vehicle.setBrand("TestBrand");
        vehicle.setModel("TestModel");
        vehicle.setPlateNumber("NS-TEST-123");
        vehicle.setSeatNumber(4);
        vehicle.setBabyFriendly(false);
        vehicle.setPetFriendly(false);
        return vehicle;
    }

    public static Driver createTestDriver(Vehicle vehicle) {
        Driver driver = new Driver();
        driver.setEmail("testdriver@example.com");
        driver.setPassword("passwordDriver");
        driver.setFirstName("Test");
        driver.setLastName("Driver");
        driver.setAddress("Driver Address");
        driver.setPhone("+381611111111");
        driver.setAccountActivated(true);
        driver.setAccountBlocked(false);
        driver.setAvailable(true);
        driver.setWorkingHoursToday(1);
        driver.setVehicle(vehicle);
        driver.setCreatedAt(LocalDateTime.now());
        driver.setRole(UserRole.DRIVER);
        return driver;
    }

    public static ActiveVehicle createActiveVehicle(Vehicle vehicle) {
        ActiveVehicle activeVehicle = new ActiveVehicle();
        activeVehicle.setVehicle(vehicle);

        Location location = new Location();
        location.setLatitude(45.2671);
        location.setLongitude(19.8335);
        location.setAddress("ĆУлица Кисачка 82, Нови Сад, 21101 Нови Сад Нови Сад Србија");
        activeVehicle.setLocation(location);
        activeVehicle.setAvailable(true);
        activeVehicle.setRouteCoordinates(null);
        activeVehicle.setRouteIndex(0);
        activeVehicle.setRide(null);

        return activeVehicle;
    }

}