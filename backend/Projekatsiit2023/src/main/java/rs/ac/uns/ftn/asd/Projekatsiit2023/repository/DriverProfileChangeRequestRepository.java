package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.DriverUpdateRequestStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.DriverProfileChangeRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverProfileChangeRequestRepository extends JpaRepository<DriverProfileChangeRequest, Long> {
    boolean existsById(Long id);
    Optional<DriverProfileChangeRequest> findById(Long id);
    List<DriverProfileChangeRequest> findAllByStatusOrderByCreatedAtDesc(DriverUpdateRequestStatus status);

}
