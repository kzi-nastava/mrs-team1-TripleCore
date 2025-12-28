package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common;

public class LocationDTO {
    private final double latitude;
    private final double longitude;

    public LocationDTO(double lat, double lon){
        latitude = lat;
        longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
