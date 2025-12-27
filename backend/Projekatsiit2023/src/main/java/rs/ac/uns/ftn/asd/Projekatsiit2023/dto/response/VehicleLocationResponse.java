package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

public class VehicleLocationResponse {

    private double latitude;
    private double longitude;
    private boolean available;

    public VehicleLocationResponse() {
    }

    public VehicleLocationResponse(double latitude, double longitude, boolean available) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.available = available;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isAvailable() {
        return available;
    }
}
