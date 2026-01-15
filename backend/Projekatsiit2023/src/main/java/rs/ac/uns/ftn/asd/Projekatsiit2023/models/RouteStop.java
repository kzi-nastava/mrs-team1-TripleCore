package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// This is sort of a wrapper class for the embeddable Location class
// Since the Route class stores an ordered list of Locations
@Entity
@Table(name = "route_stops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Location location;

    private int stopOrder;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    @JsonBackReference
    private Route route;
}

