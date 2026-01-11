package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
public class Driver extends User{

    @Column(nullable = false)
    private boolean isCurrentlyWorking;

    @Column(nullable = false)
    private double workingHoursToday;

    private LocalDateTime lastWorkStart;

    private boolean isAvailable;

    @OneToOne   // automatically recognizes it as a foreign key
    @JoinColumn(name = "vehicle_id", unique = true)
    private Vehicle vehicle;
}
