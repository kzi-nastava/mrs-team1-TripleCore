package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideEstimateRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideEstimateResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.RouteStop;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RouteRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RouteService;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    // Constants for calculations
    private static final double AVERAGE_SPEED_KMH = 40.0;
    private static final int EARTH_RADIUS_KM = 6371;

    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public RideEstimateResponse calculateRoute(RideEstimateRequest request) {
        // calculate distance
        double distance = calculateDistance(
                request.getStartLat(), request.getStartLon(),
                request.getEndLat(), request.getEndLon()
        );

        // estimate time
        int estimatedTime = calculateEstimatedTime(distance);

        // generate route coordinates
        String routeCoordinates = generateRouteCoordinates(
                request.getStartLat(), request.getStartLon(),
                request.getEndLat(), request.getEndLon()
        );

        String message = String.format("Udaljenost: %.1f km | Procenjeno vreme: %d min",
                distance, estimatedTime);

        return new RideEstimateResponse(
                estimatedTime,
                distance,
                routeCoordinates,
                message
        );
    }

    @Override
    public Route createTestRoute() {
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
        route.setEstimatedDistanceMeters(6400.0);  // 6.4 km u metrima
        route.setEstimatedDurationSeconds(1080L); // 18 minuta

        route.getStops().add(stop1);
        route.getStops().add(stop2);

        stop1.setRoute(route);
        stop2.setRoute(route);

        return routeRepository.save(route);
    }

    @Override
    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route with id: " + id + " not found"));
    }

    private double calculateDistance(double startLat, double startLon,
                                     double endLat, double endLon) {
        double lat1 = Math.toRadians(startLat);
        double lon1 = Math.toRadians(startLon);
        double lat2 = Math.toRadians(endLat);
        double lon2 = Math.toRadians(endLon);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // in km
    }

    private int calculateEstimatedTime(double distance) {
        double timeInHours = distance / AVERAGE_SPEED_KMH;
        int timeInMinutes = (int) Math.round(timeInHours * 60);
        return Math.max(5, timeInMinutes);
    }

    private String generateRouteCoordinates(double startLat, double startLon,
                                            double endLat, double endLon) {
        return String.format("%.6f,%.6f;%.6f,%.6f",
                startLat, startLon, endLat, endLon);
    }
}