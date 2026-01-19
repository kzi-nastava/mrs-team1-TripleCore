package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private double latitude;
    private double longitude;
    private String address;

    @Override
    public String toString() {
        return address + " (" + latitude + ", " + longitude + ")";
    }

}
