package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    boolean existsByDriverAndStatus(Driver driver, RideStatus status);
    boolean existsByDriverIdAndStatus(Long driverId, RideStatus status);
}
