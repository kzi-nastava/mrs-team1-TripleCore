package model;

import java.time.LocalDateTime;
import java.io.Serializable;

public class Panic implements Serializable {
    private Long id;
    private String driverName;
    private String passengerName;
    private String time;
    private boolean resolved;
    private String vehicle;
    private String location;
    private String licensePlate;

    public Panic() {}

    public Panic(Long id, String driverName, String passengerName, String time,
                 boolean resolved, String vehicle, String location, String licensePlate) {
        this.id = id;
        this.driverName = driverName;
        this.passengerName = passengerName;
        this.time = time;
        this.resolved = resolved;
        this.vehicle = vehicle;
        this.location = location;
        this.licensePlate = licensePlate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }

    public String getVehicle() { return vehicle; }
    public void setVehicle(String vehicle) { this.vehicle = vehicle; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
}