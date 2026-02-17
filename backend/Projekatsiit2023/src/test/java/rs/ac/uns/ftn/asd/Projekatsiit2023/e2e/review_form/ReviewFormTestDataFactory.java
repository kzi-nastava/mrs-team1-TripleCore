package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.review_form;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;

import java.time.LocalDateTime;

public class ReviewFormTestDataFactory {

    public static Passenger createTestPassenger() {
        Passenger p = new Passenger();

        p.setEmail("testpassenger@example.com");
        p.setPassword("passwordPassenger");
        p.setFirstName("Test");
        p.setLastName("Passenger");
        p.setAddress("Test Address 1");
        p.setPhone("+381600000000");
        p.setRole(UserRole.PASSENGER);
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now());
        p.setProfileImage(null);

        return p;
    }

    public static Vehicle createTestVehicle() {
        Vehicle v = new Vehicle();

        v.setBrand("Toyota");
        v.setModel("Corolla");
        v.setPlateNumber("NS-123-AB");
        v.setSeatNumber(4);
        v.setBabyFriendly(true);
        v.setPetFriendly(false);
        v.setType(VehicleType.STANDARD);

        return v;
    }

    public static Driver createTestDriver(Vehicle vehicle) {
        Driver d = new Driver();

        d.setEmail("test.driver@example.com");
        d.setPassword("passwordDriver");
        d.setFirstName("Test");
        d.setLastName("Driver");
        d.setAddress("Driver Address 1");
        d.setPhone("+381611111111");
        d.setRole(UserRole.DRIVER);
        d.setAccountActivated(true);
        d.setAccountBlocked(false);
        d.setCreatedAt(LocalDateTime.now());
        d.setProfileImage(null);

        d.setCurrentlyWorking(false);
        d.setWorkingHoursToday(0.0);
        d.setLastWorkStart(null);
        d.setAvailable(true);

        d.setVehicle(vehicle);

        return d;
    }

    public static Route createTestRoute() {
        Route route = new Route();

        Location start = new Location();
        start.setLatitude(45.2671);
        start.setLongitude(19.8335);
        start.setAddress("Bulevar Oslobođenja 1, Novi Sad");

        Location end = new Location();
        end.setLatitude(45.2550);
        end.setLongitude(19.8450);
        end.setAddress("Liman 4, Novi Sad");

        route.setStartLocation(start);
        route.setEndLocation(end);

        route.setEstimatedDurationSeconds(900L); // 15 min
        route.setEstimatedDistanceMeters(4500);  // 4.5 km

        return route;
    }

    public static Ride createTestRide(Passenger passenger, Driver driver, Route route) {
        Ride ride = new Ride();

        ride.setOrderer(passenger);
        ride.setDriver(driver);
        ride.setRoute(route);
        ride.setStartTime(LocalDateTime.now());
        ride.setStatus(RideStatus.FINISHED);
        ride.setBabyFriendly(false);
        ride.setPetFriendly(false);
        ride.setPanic(false);
        ride.setInconsistencies(null);
        ride.setPrice(null);
        ride.setEndTime(null);
        ride.setActualEndLocation(null);
        ride.setPanicTriggeredBy(null);
        ride.setPanicTriggeredAt(null);
        ride.setCancelledBy(null);
        ride.setEndTime(LocalDateTime.now().plusMinutes(15));
        ride.setPrice(850.0);
        ride.setActualEndLocation(route.getEndLocation());

        return ride;
    }
}
