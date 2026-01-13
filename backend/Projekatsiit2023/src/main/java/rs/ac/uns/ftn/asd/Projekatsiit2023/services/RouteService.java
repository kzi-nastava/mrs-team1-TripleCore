package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.RouteStop;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RouteRepository;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository rr){
        this.routeRepository = rr;
    }

    public Route CreateTestRoute(){
        Location start = new Location();
        start.setLatitude(45.2671);
        start.setLongitude(19.8335);
        start.setAddress("Bulevar Oslobođenja 45, Novi Sad");

        Location end = new Location();
        end.setLatitude(45.2517);
        end.setLongitude(19.8369);
        end.setAddress("Trg Slobode 1, Novi Sad");

        RouteStop stop1 = new RouteStop();
        Location stop1Loc = new Location();
        stop1Loc.setLatitude(45.2600);
        stop1Loc.setLongitude(19.8350);
        stop1Loc.setAddress("Futoški put 25, Novi Sad");
        stop1.setLocation(stop1Loc);
        stop1.setStopOrder(1);

        RouteStop stop2 = new RouteStop();
        Location stop2Loc = new Location();
        stop2Loc.setLatitude(45.2550);
        stop2Loc.setLongitude(19.8375);
        stop2Loc.setAddress("Narodnog Fronta 12, Novi Sad");
        stop2.setLocation(stop2Loc);
        stop2.setStopOrder(2);

        Route route = new Route();
        route.setStartLocation(start);
        route.setEndLocation(end);
        route.setEstimatedDistanceMeters(6.4);  // test vrednost
        route.setEstimatedDurationSeconds(1080L); // 18 minuta

        route.getStops().add(stop1);
        route.getStops().add(stop2);

        stop1.setRoute(route);
        stop2.setRoute(route);

        routeRepository.save(route);
        return route;
    }

    public Route getRouteById(Long id){
        return routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route with id: " + id + " not found"));
    }
}
