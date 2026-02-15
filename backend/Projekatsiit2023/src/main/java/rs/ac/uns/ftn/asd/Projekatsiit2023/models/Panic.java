package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "panics")
public class
Panic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String driverName;
    private String passengerName;

    private LocalDateTime time;

    private boolean resolved = false;

    private String vehicle;
    private String location;
    private String licensePlate;

}
