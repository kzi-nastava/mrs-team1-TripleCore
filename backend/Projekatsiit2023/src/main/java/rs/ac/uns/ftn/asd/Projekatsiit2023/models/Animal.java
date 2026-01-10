package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.*;

@Entity
@Table(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;

    public Animal(){

    }

    public Animal(String name, String species) {
        this.name = name;
        this.species = species;
    }
}
