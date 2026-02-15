package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.UserBlock;

import java.util.Optional;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    Optional<UserBlock> findByUserId(Long userId);
}
