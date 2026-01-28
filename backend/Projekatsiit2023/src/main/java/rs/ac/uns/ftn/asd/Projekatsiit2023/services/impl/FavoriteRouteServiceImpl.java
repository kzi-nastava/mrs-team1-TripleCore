package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.FavoriteRouteResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.FavoriteRouteRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RouteRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.FavoriteRouteService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteRouteServiceImpl  implements FavoriteRouteService {
    private final FavoriteRouteRepository favoriteRouteRepository;
    private final RouteRepository routeRepository;

    public FavoriteRouteServiceImpl(FavoriteRouteRepository favoriteRouteRepository, RouteRepository routeRepository) {
        this.favoriteRouteRepository = favoriteRouteRepository;
        this.routeRepository = routeRepository;
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

}
