package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    // insert delete i find vec postoje
}
