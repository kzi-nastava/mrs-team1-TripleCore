package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.LocationDTO;

public class ActiveVehicleLocationResponse {

    private final LocationDTO location;
    @Getter
    private final boolean available;

    public ActiveVehicleLocationResponse(double latitude, double longitude, boolean available) {
        this.location = new LocationDTO(latitude, longitude);
        this.available = available;
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return this.location.getLongitude();
    }

}
