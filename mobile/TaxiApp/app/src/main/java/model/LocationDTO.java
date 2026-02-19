package model;

public class LocationDTO {
    public double latitude;
    public double longitude;
    public String address;

    public LocationDTO(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationDTO() {
    }

}