package model;

public class StopRideRequest {

    public double latitude;
    public double longitude;
    public String address;

    public StopRideRequest(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
