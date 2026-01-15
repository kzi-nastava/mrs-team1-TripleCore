package rs.ac.uns.ftn.asd.Projekatsiit2023.test;

import org.hibernate.dialect.SpannerSqlAstTranslator;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RouteRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.DriverService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.PassengerService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestService {
    private final DriverService driverService;
    private final PassengerService passengerService;
    private final RideRepository rideRepository;
    private final RouteRepository routeRepository;

    public  TestService(
            DriverService driverService,
            RideRepository rideRepository,
            PassengerService passengerService,
            RouteRepository routeRepository){
        this.driverService = driverService;
        this.rideRepository = rideRepository;
        this.passengerService = passengerService;
        this.routeRepository = routeRepository;
    }


    public void generateMockRides() {
        Driver driver = driverService.getDriverById(1L);
        Passenger passenger = passengerService.getPassengerById(2L);

        // Kreiranje stub Locations
        Location startLocation = new Location(45.2671, 19.8335, "Start Address");
        Location endLocation1 = new Location(45.2550, 19.8450, "End Address 1");
        Location endLocation2 = new Location(45.2600, 19.8500, "End Address 2");

        // --- Kreiranje Route objekata ---
        Route route1 = new Route();
        route1.setStartLocation(startLocation);
        route1.setEndLocation(endLocation1);

        RouteStop stop1a = new RouteStop();
        stop1a.setLocation(startLocation);
        stop1a.setStopOrder(0);
        stop1a.setRoute(route1); // OBAVEZNO!

        RouteStop stop1b = new RouteStop();
        stop1b.setLocation(endLocation1);
        stop1b.setStopOrder(1);
        stop1b.setRoute(route1); // OBAVEZNO!

        route1.setStops(List.of(stop1a, stop1b));

        Route route2 = new Route();
        route2.setStartLocation(startLocation);
        route2.setEndLocation(endLocation2);

        RouteStop stop2a = new RouteStop();
        stop2a.setLocation(startLocation);
        stop2a.setStopOrder(0);
        stop2a.setRoute(route2); // OBAVEZNO!

        RouteStop stop2b = new RouteStop();
        stop2b.setLocation(endLocation2);
        stop2b.setStopOrder(1);
        stop2b.setRoute(route2); // OBAVEZNO!

        route2.setStops(List.of(stop2a, stop2b));

        // Sačuvaj rute (Cascade.ALL će automatski sačuvati RouteStop)
        routeRepository.save(route1);
        routeRepository.save(route2);

        // --- Kreiranje 2 Ride objekta ---
        Ride ride1 = new Ride();
        ride1.setDriver(driver);
        ride1.setOrderer(passenger);
        ride1.setLinkedPassengers(List.of()); // nema linked passengers
        ride1.setRoute(route1);
        ride1.setStartTime(LocalDateTime.now().minusHours(2));
        ride1.setEndTime(LocalDateTime.now().minusHours(1));
        ride1.setActualEndLocation(endLocation1);
        ride1.setPrice(500.0);
        ride1.setBabyFriendly(true);
        ride1.setPetFriendly(false);
        ride1.setInconsistencies(null);
        ride1.setStatus(RideStatus.FINISHED);
        ride1.setPanic(false);

        Ride ride2 = new Ride();
        ride2.setDriver(driver);
        ride2.setOrderer(passenger);
        ride2.setLinkedPassengers(List.of()); // nema linked passengers
        ride2.setRoute(route2);
        ride2.setStartTime(LocalDateTime.now().minusHours(3));
        ride2.setEndTime(LocalDateTime.now().minusHours(2));
        ride2.setActualEndLocation(endLocation2);
        ride2.setPrice(750.0);
        ride2.setBabyFriendly(false);
        ride2.setPetFriendly(true);
        ride2.setInconsistencies("Passenger complained about AC");
        ride2.setStatus(RideStatus.CANCELLED);
        ride2.setPanic(false);

        // Sačuvaj vožnje
        rideRepository.save(ride1);
        rideRepository.save(ride2);
    }

}
