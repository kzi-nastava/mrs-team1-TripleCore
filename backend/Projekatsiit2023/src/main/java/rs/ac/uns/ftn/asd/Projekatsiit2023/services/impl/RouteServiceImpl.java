package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideEstimateRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.GraphHopperResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideEstimateResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.RouteStop;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RouteRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RouteService;

import java.util.*;

@Slf4j
@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

//    private static final String GRAPHHOPPER_API_KEY = "5dbed131-8591-46a7-af62-dc68f408e453";
    private static final String GRAPHHOPPER_API_KEY = "f7dabe5f-5827-487d-8e31-9dce646443ab"; // Aleksandrov key

    private static final String GRAPHHOPPER_URL = "https://graphhopper.com/api/1/route";

    public RouteServiceImpl(RouteRepository routeRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.routeRepository = routeRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public RideEstimateResponse calculateRoute(RideEstimateRequest request) {
        try {
            GraphHopperResponse ghResponse = callGraphHopperAPI(request);

            if (ghResponse != null && ghResponse.getPaths() != null && !ghResponse.getPaths().isEmpty()) {
                return extractRouteFromGraphHopper(ghResponse, request);
            } else {
                throw new RuntimeException("GraphHopper returned empty response");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate route: " + e.getMessage());
        }
    }

    // calls GraphHopper API and returns the parsed response
    private GraphHopperResponse callGraphHopperAPI(RideEstimateRequest request) {
        try {
            // Construct the URL with parameters
            String url = String.format(Locale.US,
                    "%s?point=%f,%f&point=%f,%f&vehicle=car&locale=en&key=%s&instructions=false&calc_points=true&points_encoded=false",
                    GRAPHHOPPER_URL,
                    request.getStartLat(), request.getStartLon(),
                    request.getEndLat(), request.getEndLon(),
                    GRAPHHOPPER_API_KEY);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return objectMapper.readValue(response.getBody(), GraphHopperResponse.class);
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error("Error calling GraphHopper API: {}", e.getMessage());
            return null;
        }
    }

    // extracts distance, time and coordinates from GraphHopper response
    private RideEstimateResponse extractRouteFromGraphHopper(GraphHopperResponse ghResponse, RideEstimateRequest request) {
        GraphHopperResponse.Path path = ghResponse.getPaths().get(0);

        // distance in km
        double distance = path.getDistance() / 1000.0;

        // time in minutes
        int estimatedTime = (int) (path.getTime() / 1000.0 / 60.0);

        // extract coordinates
        String routeCoordinates = extractCoordinatesFromGraphHopper(path);

        if (routeCoordinates.isEmpty()) {
            throw new RuntimeException("No coordinates extracted from GraphHopper");
        }

        String message = String.format(Locale.US,
                "Distance: %.1f km | Estimated time: %d min", distance, estimatedTime);

        return new RideEstimateResponse(estimatedTime, distance, routeCoordinates, message);
    }

    // extracts coordinates from GraphHopper path
    private String extractCoordinatesFromGraphHopper(GraphHopperResponse.Path path) {
        if (path.getPoints() == null || path.getPoints().getCoordinates() == null) {
            log.warn("No coordinates in GraphHopper response");
            return "";
        }

        List<List<Double>> coordinates = path.getPoints().getCoordinates();
        log.info("GraphHopper returned {} coordinate points", coordinates.size());

        StringBuilder sb = new StringBuilder();

        // GraphHopper returns coordinates as [lon, lat]
        for (List<Double> coord : coordinates) {
            if (coord.size() >= 2) {
                double longitude = coord.get(0); 
                double latitude = coord.get(1);  

                // format: "lat,lon;"
                sb.append(String.format(Locale.US, "%.6f,%.6f;", latitude, longitude));
            }
        }

        // removing last ';'
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
            log.info("Extracted {} coordinate pairs", coordinates.size());
            return sb.toString();
        }

        return "";
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
        route.setEstimatedDistanceMeters(6400.0);
        route.setEstimatedDurationSeconds(1080L);

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


    private String buildGraphHopperUrl(List<Location> points) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("At least two points are required to calculate a route");
        }

        StringBuilder url = new StringBuilder(GRAPHHOPPER_URL);
        url.append("?");

        for (Location point : points) {
            url.append(String.format(
                    Locale.US,
                    "point=%f,%f&",
                    point.getLatitude(),
                    point.getLongitude()
            ));
        }

        url.append("vehicle=car");
        url.append("&locale=en");
        url.append("&instructions=false");
        url.append("&calc_points=true");
        url.append("&points_encoded=false");
        url.append("&key=").append(GRAPHHOPPER_API_KEY);

        return url.toString();
    }

    private GraphHopperResponse callGraphHopperForMultiplePoints(List<Location> points) {
        try {
            String url = buildGraphHopperUrl(points);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return objectMapper.readValue(response.getBody(), GraphHopperResponse.class);
            }

            log.error("GraphHopper returned empty or non-OK response");
            return null;

        } catch (Exception e) {
            log.error("Error calling GraphHopper with multiple points", e);
            return null;
        }
    }

    public List<Location> convertRouteToLocationList(Route route){
        List<Location> points = new ArrayList<>();
        points.add(route.getStartLocation());
        if (!route.getStops().isEmpty()){
            for (RouteStop routeStop : route.getStops()){
                points.add(routeStop.getLocation());
            }
        }
        points.add(route.getEndLocation());
        return points;
    }

    public String calculateRouteThroughPoints(List<Location> points) {

        GraphHopperResponse ghResponse = callGraphHopperForMultiplePoints(points);

        if (ghResponse == null || ghResponse.getPaths() == null || ghResponse.getPaths().isEmpty()) {
            throw new RuntimeException("No route returned from GraphHopper");
        }

        GraphHopperResponse.Path path = ghResponse.getPaths().get(0);

        return extractCoordinatesFromGraphHopper(path);
    }

    public Location getRandomNoviSadLocation(){
        double minLat = 45.2300;
        double maxLat = 45.2800;
        double minLon = 19.8000;
        double maxLon = 19.8800;

        double latitude = minLat + (Math.random() * (maxLat - minLat));
        double longitude = minLon + (Math.random() * (maxLon - minLon));

        return new Location(latitude, longitude, null);
    }
    
    public int calculateDistanceBetweenTwoPoints(Location from, Location to) {

        final double EARTH_RADIUS_METERS = 6371000;

        double lat1 = Math.toRadians(from.getLatitude());
        double lon1 = Math.toRadians(from.getLongitude());
        double lat2 = Math.toRadians(to.getLatitude());
        double lon2 = Math.toRadians(to.getLongitude());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceMeters = EARTH_RADIUS_METERS * c;

        return (int) Math.round(distanceMeters);
    }



}