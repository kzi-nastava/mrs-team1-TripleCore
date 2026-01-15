package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rides")
@Getter
@Setter
@NoArgsConstructor
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "orderer_id", nullable = false)
    private Passenger orderer;

    @ManyToMany
    @JoinTable(
            name = "linked_passengers",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id")
    )
    private List<Passenger> linkedPassengers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "actual_end_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "actual_end_lon")),
            @AttributeOverride(name = "address", column = @Column(name = "actual_end_address"))
    })
    private Location actualEndLocation;

    private LocalDateTime endTime;
    private Double price;
    private boolean babyFriendly;
    private boolean petFriendly;
    private String inconsistencies;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status;
}
