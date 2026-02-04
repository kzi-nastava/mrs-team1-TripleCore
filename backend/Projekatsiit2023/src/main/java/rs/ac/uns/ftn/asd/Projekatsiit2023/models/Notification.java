package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    // This should tell the frontend or mobile app what to open
    // for example "ride-tracking:3" tells the frontend to open active ride tracking for ride 3
    // or "review:5" means link them to the review form for finished ride 5
    private String link;

    private String title;

    private String message;

    private LocalDateTime time;

    private boolean seen;
}
