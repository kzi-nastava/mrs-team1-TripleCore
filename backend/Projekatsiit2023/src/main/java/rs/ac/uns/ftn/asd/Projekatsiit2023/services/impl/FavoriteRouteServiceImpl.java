package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.FavoriteRouteResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.FavoriteRoute;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.FavoriteRouteRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RouteRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.FavoriteRouteService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteRouteServiceImpl  implements FavoriteRouteService {
    private final FavoriteRouteRepository favoriteRouteRepository;
    private final RouteRepository routeRepository;

    private RideRepository rideRepository;

    public FavoriteRouteServiceImpl(FavoriteRouteRepository favoriteRouteRepository, RouteRepository routeRepository, RideRepository rideRepository) {
        this.favoriteRouteRepository = favoriteRouteRepository;
        this.routeRepository = routeRepository;
        this.rideRepository = rideRepository;
    }

    @Override
    public List<FavoriteRouteResponse> getFavoriteRoutesByUserId(Long userId) {
        return favoriteRouteRepository.findByUserId(userId)
                .stream()
                .map(fav -> {
                    Route route = routeRepository.findById(fav.getRouteId())
                            .orElseThrow(() -> new RuntimeException("Route not found"));
                    return mapToDto(route);
                })
                .collect(Collectors.toList());
    }

    private FavoriteRouteResponse mapToDto(Route route) {
        return new FavoriteRouteResponse(
                route.getId(),
                route.getStartLocation().getAddress(),
                route.getStartLocation().getLatitude(),
                route.getStartLocation().getLongitude(),
                route.getEndLocation().getAddress(),
                route.getEndLocation().getLatitude(),
                route.getEndLocation().getLongitude(),
                route.getEstimatedDistanceMeters(),
                route.getEstimatedDurationSeconds()
        );
    }

    @Override
    public void addFavoriteRoute(Long passengerId, Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        Long routeId = ride.getRoute().getId();


        boolean exists = favoriteRouteRepository
                .existsByUserIdAndRouteId(passengerId, routeId);

        if (exists) {
            throw new RuntimeException("Route already in favorites");
        }

        FavoriteRoute favoriteRoute = new FavoriteRoute(passengerId, routeId);
        favoriteRouteRepository.save(favoriteRoute);
    }

    @Override
    public void removeFavoriteRoute(Long passengerId, Long routeId) {
        FavoriteRoute favoriteRoute = favoriteRouteRepository
                .findByUserIdAndRouteId(passengerId, routeId)
                .orElseThrow(() -> new RuntimeException("Favorite route not found"));

        favoriteRouteRepository.delete(favoriteRoute);
    }

}
