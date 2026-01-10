package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
public class Driver extends User{

    @Column(nullable = false)
    private boolean isCurrentlyWorking;

    @Column(nullable = false)
    private double workingHoursToday;

    private LocalDateTime lastStatusChange;

    private Long vehicleId;
}
