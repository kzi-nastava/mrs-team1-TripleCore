package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideEstimateRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideEstimateResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Route;

public interface RouteService {
    RideEstimateResponse calculateRoute(RideEstimateRequest request);
    Route createTestRoute();
    Route getRouteById(Long id);
    int calculateDistanceBetweenTwoPoints(Location from, Location to);
    Location getRandomNoviSadLocation();
}