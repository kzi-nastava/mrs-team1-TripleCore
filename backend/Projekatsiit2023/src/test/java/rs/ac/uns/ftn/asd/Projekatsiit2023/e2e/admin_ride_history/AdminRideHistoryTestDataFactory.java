package rs.ac.uns.ftn.asd.Projekatsiit2023.e2e.admin_ride_history;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;

import java.time.LocalDateTime;

public class AdminRideHistoryTestDataFactory {
    public static Route createTestRoute1() {
        Route r = new Route();

        Location start = new Location();
        start.setLatitude(45.2671);
        start.setLongitude(19.8335);
        start.setAddress("Bulevar Oslobođenja 1, Novi Sad");

        Location end = new Location();
        end.setLatitude(45.2550);
        end.setLongitude(19.8450);
        end.setAddress("Futoška 10, Novi Sad");

        r.setStartLocation(start);
        r.setEndLocation(end);
        r.setEstimatedDurationSeconds(900L); // 15 min
        r.setEstimatedDistanceMeters(3500.0);

        return r;
    }

    public static Route createTestRoute2() {
        Route r = new Route();

        Location start = new Location();
        start.setLatitude(44.7866);
        start.setLongitude(20.4489);
        start.setAddress("Knez Mihailova 5, Beograd");

        Location end = new Location();
        end.setLatitude(44.8125);
        end.setLongitude(20.4612);
        end.setAddress("Bulevar Kralja Aleksandra 73, Beograd");

        r.setStartLocation(start);
        r.setEndLocation(end);
        r.setEstimatedDurationSeconds(1200L); // 20 min
        r.setEstimatedDistanceMeters(5200.0);

        return r;
    }

    public static Route createTestRoute3() {
        Route r = new Route();

        Location start = new Location();
        start.setLatitude(43.3209);
        start.setLongitude(21.8958);
        start.setAddress("Obrenovićeva 12, Niš");

        Location end = new Location();
        end.setLatitude(43.3247);
        end.setLongitude(21.9033);
        end.setAddress("Čair Park, Niš");

        r.setStartLocation(start);
        r.setEndLocation(end);
        r.setEstimatedDurationSeconds(600L); // 10 min
        r.setEstimatedDistanceMeters(1800.0);

        return r;
    }

    public static Vehicle createTestVehicle1() {
        Vehicle v = new Vehicle();

        v.setBrand("Toyota");
        v.setModel("Corolla");
        v.setPlateNumber("NS-123-AA");
        v.setSeatNumber(4);
        v.setBabyFriendly(false);
        v.setPetFriendly(true);
        v.setType(VehicleType.STANDARD);

        return v;
    }

    public static Vehicle createTestVehicle2() {
        Vehicle v = new Vehicle();

        v.setBrand("Mercedes");
        v.setModel("E220");
        v.setPlateNumber("BG-456-BB");
        v.setSeatNumber(4);
        v.setBabyFriendly(true);
        v.setPetFriendly(false);
        v.setType(VehicleType.LUXURY);

        return v;
    }

    public static Vehicle createTestVehicle3() {
        Vehicle v = new Vehicle();

        v.setBrand("Volkswagen");
        v.setModel("Transporter");
        v.setPlateNumber("NI-789-CC");
        v.setSeatNumber(8);
        v.setBabyFriendly(true);
        v.setPetFriendly(true);
        v.setType(VehicleType.VAN);

        return v;
    }

    public static Driver createTestDriver1() {
        Driver d = new Driver();

        d.setEmail("driver1@example.com");
        d.setPassword("password123");
        d.setFirstName("Petar");
        d.setLastName("Petrovic");
        d.setAddress("Bulevar Oslobođenja 1, Novi Sad");
        d.setPhone("+38160111222");
        d.setRole(UserRole.DRIVER);
        d.setAccountActivated(true);
        d.setAccountBlocked(false);
        d.setCreatedAt(LocalDateTime.now().minusDays(10));

        d.setCurrentlyWorking(false);
        d.setWorkingHoursToday(0.0);
        d.setLastWorkStart(null);
        d.setAvailable(true);

        return d;
    }

    public static Driver createTestDriver2() {
        Driver d = new Driver();

        d.setEmail("driver2@example.com");
        d.setPassword("password123");
        d.setFirstName("Marko");
        d.setLastName("Markovic");
        d.setAddress("Knez Mihailova 5, Beograd");
        d.setPhone("+38160222333");
        d.setRole(UserRole.DRIVER);
        d.setAccountActivated(true);
        d.setAccountBlocked(false);
        d.setCreatedAt(LocalDateTime.now().minusDays(5));

        d.setCurrentlyWorking(true);
        d.setWorkingHoursToday(4.5);
        d.setLastWorkStart(LocalDateTime.now().minusHours(2));
        d.setAvailable(false);

        return d;
    }

    public static Driver createTestDriver3() {
        Driver d = new Driver();

        d.setEmail("driver3@example.com");
        d.setPassword("password123");
        d.setFirstName("Nikola");
        d.setLastName("Nikolic");
        d.setAddress("Obrenovićeva 12, Niš");
        d.setPhone("+38160333444");
        d.setRole(UserRole.DRIVER);
        d.setAccountActivated(true);
        d.setAccountBlocked(false);
        d.setCreatedAt(LocalDateTime.now().minusDays(1));

        d.setCurrentlyWorking(false);
        d.setWorkingHoursToday(7.0);
        d.setLastWorkStart(LocalDateTime.now().minusHours(5));
        d.setAvailable(true);

        return d;
    }

    public static Passenger createTestPassenger1() {
        Passenger p = new Passenger();

        p.setEmail("passenger1@example.com");
        p.setPassword("password123");
        p.setFirstName("Ana");
        p.setLastName("Anic");
        p.setAddress("Bulevar Oslobođenja 15, Novi Sad");
        p.setPhone("+38161111222");
        p.setRole(UserRole.PASSENGER);
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now().minusDays(7));

        return p;
    }

    public static Passenger createTestPassenger2() {
        Passenger p = new Passenger();

        p.setEmail("passenger2@example.com");
        p.setPassword("password123");
        p.setFirstName("Jelena");
        p.setLastName("Jovanovic");
        p.setAddress("Kralja Petra 8, Beograd");
        p.setPhone("+38162222333");
        p.setRole(UserRole.PASSENGER);
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now().minusDays(3));

        return p;
    }

    public static Passenger createTestPassenger3() {
        Passenger p = new Passenger();

        p.setEmail("passenger3@example.com");
        p.setPassword("password123");
        p.setFirstName("Milan");
        p.setLastName("Milic");
        p.setAddress("Cara Dušana 21, Niš");
        p.setPhone("+38163333444");
        p.setRole(UserRole.PASSENGER);
        p.setAccountActivated(true);
        p.setAccountBlocked(false);
        p.setCreatedAt(LocalDateTime.now().minusDays(1));

        return p;
    }

    public static Ride createCompletedRide(
            Passenger orderer,
            Driver driver,
            Route route
    ) {
        Ride r = new Ride();

        r.setOrderer(orderer);
        r.getLinkedPassengers().add(orderer);

        r.setDriver(driver);
        r.setRoute(route);

        r.setStartTime(LocalDateTime.now().minusHours(1));
        r.setEndTime(LocalDateTime.now().minusMinutes(10));

        r.setPrice(1200.0);
        r.setBabyFriendly(false);
        r.setPetFriendly(true);
        r.setStatus(RideStatus.FINISHED);
        r.setPanic(false);

        Location actualEnd = new Location();
        actualEnd.setLatitude(route.getEndLocation().getLatitude());
        actualEnd.setLongitude(route.getEndLocation().getLongitude());
        actualEnd.setAddress(route.getEndLocation().getAddress());
        r.setActualEndLocation(actualEnd);

        return r;
    }

    public static Ride createInProgressRide(
            Passenger orderer,
            Driver driver,
            Route route
    ) {
        Ride r = new Ride();

        r.setOrderer(orderer);
        r.getLinkedPassengers().add(orderer);

        r.setDriver(driver);
        r.setRoute(route);

        r.setStartTime(LocalDateTime.now().minusMinutes(20));
        r.setEndTime(null);

        r.setPrice(null);
        r.setBabyFriendly(true);
        r.setPetFriendly(false);
        r.setStatus(RideStatus.IN_PROGRESS);
        r.setPanic(false);

        return r;
    }

    public static Ride createCancelledRide(
            Passenger orderer,
            Driver driver,
            Route route
    ) {
        Ride r = new Ride();

        r.setOrderer(orderer);
        r.getLinkedPassengers().add(orderer);

        r.setDriver(driver);
        r.setRoute(route);

        r.setStartTime(LocalDateTime.now().minusHours(2));
        r.setEndTime(LocalDateTime.now().minusHours(1));

        r.setPrice(0.0);
        r.setBabyFriendly(false);
        r.setPetFriendly(false);
        r.setStatus(RideStatus.CANCELLED);

        r.setPanic(false);

        r.setCancelledBy(orderer);
        r.setInconsistencies("Family emergency.");

        return r;
    }

    public static Admin createTestAdmin() {
        Admin admin = new Admin();

        admin.setEmail("admin@example.com");
        admin.setPassword("admin");
        admin.setFirstName("System");
        admin.setLastName("Administrator");
        admin.setAddress("Head Office");
        admin.setPhone("+38160000000");
        admin.setRole(UserRole.ADMIN);
        admin.setAccountActivated(true);
        admin.setAccountBlocked(false);
        admin.setCreatedAt(LocalDateTime.now());

        return admin;
    }



}
