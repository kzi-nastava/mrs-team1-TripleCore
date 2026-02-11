package model;

import java.io.Serializable;

public class ReviewDTO implements Serializable {

    public Long rideId;
    public Long passengerId;
    public String passengerName;

    public Long driverId;
    public String driverName;

    public int driverRating;
    public int vehicleRating;

    public String comment;

}
