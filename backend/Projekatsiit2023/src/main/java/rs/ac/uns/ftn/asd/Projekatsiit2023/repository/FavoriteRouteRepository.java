package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.FavoriteRoute;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute, Long> {
    List<FavoriteRoute> findByUserId(Long userId);

    Optional<FavoriteRoute> findByUserIdAndRouteId(Long userId, Long routeId);

    boolean existsByUserIdAndRouteId(Long userId, Long routeId);
}
