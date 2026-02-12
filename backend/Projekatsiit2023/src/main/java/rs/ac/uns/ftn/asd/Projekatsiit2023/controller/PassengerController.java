package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.FavoriteRouteResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.FavoriteRouteService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.PassengerService;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {
    private final PassengerService passengerService;
    private final FavoriteRouteService favoriteRouteService;

    public PassengerController(PassengerService passengerService, FavoriteRouteService favoriteRouteService) {
        this.passengerService = passengerService;
        this.favoriteRouteService = favoriteRouteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPassenger(@PathVariable("id") Long id){
        Passenger passenger;
        try{
            passenger = passengerService.getPassengerById(id);
            return ResponseEntity.ok(passenger);
        }
        catch (EntityNotFoundException nfe){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nfe.getMessage());
        }

    }

    @GetMapping("/{id}/ride-history")
    public ResponseEntity<?> getPassengerRideHistory(@PathVariable Long id) {
        List<RideDetailsResponse> response = passengerService.getRideHistory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/ride-history/{rideId}")
    public ResponseEntity<?> getPassengerRideDetails(@PathVariable Long id, @PathVariable Long rideId) {
        try {
            RideDetailsResponse response = passengerService.getRideDetails(id, rideId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException nfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nfe.getMessage());
        }
    }

    @GetMapping("/{id}/favorite-routes")
    public ResponseEntity<List<FavoriteRouteResponse>> getFavoriteRoutes(@PathVariable Long id) {
        List<FavoriteRouteResponse> favorites = favoriteRouteService.getFavoriteRoutesByUserId(id);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{id}/favorite-routes/{rideId}")
    public ResponseEntity<?> addFavoriteRoute(@PathVariable Long id, @PathVariable Long rideId) {

        try {
            favoriteRouteService.addFavoriteRoute(id, rideId);
            return ResponseEntity.ok("Added to favorites");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/favorite-routes/{routeId}")
    public ResponseEntity<?> removeFavoriteRoute(@PathVariable Long id, @PathVariable Long routeId) {
        try {
            favoriteRouteService.removeFavoriteRoute(id, routeId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
