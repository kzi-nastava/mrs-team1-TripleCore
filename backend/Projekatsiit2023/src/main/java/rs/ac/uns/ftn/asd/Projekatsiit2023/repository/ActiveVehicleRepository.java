package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.ActiveVehicle;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActiveVehicleRepository extends JpaRepository<ActiveVehicle, Long> {
    List<ActiveVehicle> findByRideId(Long rideId);
    List<ActiveVehicle> findByRideIdIsNull();
    List<ActiveVehicle> findByRideIdIsNotNull();
    Optional<ActiveVehicle> findByVehicleId(Long vehicleId);

}
