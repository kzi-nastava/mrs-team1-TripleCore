package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
* Because this class uses multiple instances of the embedded class Location
* and embedded classes are not stored in a separate table
* column names need to be different for every instance
* so for the startLocation:
* address -> start_address
* latitude -> start_latitude etc.
*/

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
public class Route {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "start_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "start_lon")),
            @AttributeOverride(name = "address", column = @Column(name = "start_address"))
    })
    private Location startLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "end_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "end_lon")),
            @AttributeOverride(name = "address", column = @Column(name = "end_address"))
    })
    private Location endLocation;

    // FetchType.EAGER means the route stops load at the same time as the route
    // no need to force loading later
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("stopOrder ASC") // to make sure the stops are fetched in the right order
    @JsonManagedReference
    private List<RouteStop> stops = new ArrayList<>();

    private Long estimatedDurationSeconds;
    private double estimatedDistanceMeters;
}
