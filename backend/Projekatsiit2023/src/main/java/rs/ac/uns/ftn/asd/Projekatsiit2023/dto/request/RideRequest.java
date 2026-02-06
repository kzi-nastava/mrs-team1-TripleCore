package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request;

import jakarta.validation.constraints.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;

import java.time.LocalDateTime;
import java.util.List;

public class RideRequest {

    @NotNull(message = "Start location is required")
    private Location startLocation;

    @NotNull(message = "End location is required")
    private Location endLocation;


    private List<@NotNull(message = "Stop location cannot be null") Location> intermediateStops;


    private List<@Email(message = "Linked passenger email must be valid") String> linkedPassengerEmails;

    private boolean babyFriendly;
    private boolean petFriendly;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;



    private LocalDateTime startTime;

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public List<Location> getIntermediateStops() {
        return intermediateStops;
    }

    public void setIntermediateStops(List<Location> intermediateStops) {
        this.intermediateStops = intermediateStops;
    }

    public List<String> getLinkedPassengerEmails() {
        return linkedPassengerEmails;
    }

    public void setLinkedPassengerEmails(List<String> linkedPassengerEmails) {
        this.linkedPassengerEmails = linkedPassengerEmails;
    }

    public boolean isBabyFriendly() {
        return babyFriendly;
    }

    public void setBabyFriendly(boolean babyFriendly) {
        this.babyFriendly = babyFriendly;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String toString() {
        return "RideRequest{" +
                "startLocation=" + startLocation +
                ", endLocation=" + endLocation +
                ", intermediateStops=" + intermediateStops +
                ", linkedPassengerEmails=" + linkedPassengerEmails +
                ", babyFriendly=" + babyFriendly +
                ", petFriendly=" + petFriendly +
                ", vehicleType=" + vehicleType +
                ", startTime=" + startTime +
                '}';
    }

}
