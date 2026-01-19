package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Panic;

import java.util.List;

@Repository
public interface PanicRepository extends JpaRepository<Panic, Long> {

    List<Panic> findAllByOrderByTimeDesc();
    List<Panic> findByResolvedFalseOrderByTimeDesc();
    List<Panic> findByResolvedTrueOrderByTimeDesc();
}
