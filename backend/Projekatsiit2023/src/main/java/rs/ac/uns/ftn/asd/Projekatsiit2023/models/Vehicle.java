package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String plateNumber;

    @Column(nullable = false)
    private int seatNumber;

    @Column(nullable = false)
    private boolean babyFriendly;

    @Column(nullable = false)
    private boolean petFriendly;

    @Column(nullable = false)
    private VehicleType type;
}
