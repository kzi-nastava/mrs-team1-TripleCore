package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.FavoriteRouteResponse;

import java.util.List;

public interface FavoriteRouteService {
    List<FavoriteRouteResponse> getFavoriteRoutesByUserId(Long userId);
}
