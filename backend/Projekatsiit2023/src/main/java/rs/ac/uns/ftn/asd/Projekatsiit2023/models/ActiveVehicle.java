package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// Vehicle is active if the driver is logged in
// If the driver is working a ride the vehicle is not available
// When the driver is idle the vehicle is available
// This class exists primarily for displaying the current state on the map

@Entity
@Table(name = "active_vehicles")
@Getter
@Setter
@NoArgsConstructor
public class ActiveVehicle {

    @Id
    private Long vehicleId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Embedded
    private Location location;

    private boolean available;

    @OneToOne()
    private Ride ride;

    @Column(columnDefinition = "TEXT")
    private String routeCoordinates; // this is the route the active vehicle is moving along

    @Column(nullable = false)
    private int routeIndex = 0;

    public ActiveVehicle(Vehicle vehicle, Location location, boolean available) {
        this.vehicle = vehicle;
        this.location = location;
        this.available = available;
    }
}

