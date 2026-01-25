package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.ActiveVehicle;

import java.util.List;

@Repository
public interface ActiveVehicleRepository extends JpaRepository<ActiveVehicle, Long> {
    List<ActiveVehicle> findByRideId(Long rideId);
}
