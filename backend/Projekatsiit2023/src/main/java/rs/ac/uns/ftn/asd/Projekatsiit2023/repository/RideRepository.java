package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findAll();
    @Query("SELECT r FROM Ride r WHERE r.orderer.id = :passengerId")
    List<Ride> findByPassengerId(@Param("passengerId") Long passengerId);
    List<Ride> findByDriverId(Long driverId);
    boolean existsByDriverAndStatus(Driver driver, RideStatus status);
    boolean existsByDriverIdAndStatus(Long driverId, RideStatus status);
    boolean existsByOrdererAndStatusIn(Passenger orderer, List<RideStatus> statuses);

    List<Ride> findByDriverIdAndStatusIn(Long driverId, List<RideStatus> statuses);

    List<Ride> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);


}
