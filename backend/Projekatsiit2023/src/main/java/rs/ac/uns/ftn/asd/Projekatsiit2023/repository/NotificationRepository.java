package rs.ac.uns.ftn.asd.Projekatsiit2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findByRecipientId(Long recipientId);
}
