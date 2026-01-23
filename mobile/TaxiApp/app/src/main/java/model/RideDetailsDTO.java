package model;

import java.util.List;

public class RideDetailsDTO {

    // passengers
    public Long id;
    public String ordererName;
    public List<String> linkedPassengers;
    public String ordererProfileImage;

    // driver
    public String driverName;
    public String vehicle;
    public String driverProfileImage;

    // route
    public LocationDTO startLocation;
    public LocationDTO endLocation;
    public List<LocationDTO> routeStops;

    // time
    public String startTime;
    public String endTime;

    // panic
    public boolean panic;
    public String panicTriggeredBy;
    public String panicTriggeredAt;

    // other info
    public double price;
    public String status;
    public String cancelledBy;
    public List<ReviewDTO> reviews;
    public String inconsistencies;

}
