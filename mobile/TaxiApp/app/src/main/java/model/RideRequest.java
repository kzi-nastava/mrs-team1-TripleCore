package model;

import java.util.List;

public class RideRequest {
    private LocationDTO startLocation;
    private LocationDTO endLocation;
    private List<LocationDTO> intermediateStops;
    private List<String> linkedPassengerEmails;
    private boolean babyFriendly;
    private boolean petFriendly;
    private String vehicleType;
    private String startTime;



    public LocationDTO getStartLocation() {
        return startLocation;
    }

    public LocationDTO getEndLocation() {
        return endLocation;
    }

    public List<LocationDTO> getIntermediateStops() {
        return intermediateStops;
    }

    public List<String> getLinkedPassengerEmails() {
        return linkedPassengerEmails;
    }

    public boolean isBabyFriendly() {
        return babyFriendly;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartLocation(LocationDTO startLocation) {
        this.startLocation = startLocation;
    }

    public void setEndLocation(LocationDTO endLocation) {
        this.endLocation = endLocation;
    }

    public void setIntermediateStops(List<LocationDTO> intermediateStops) {
        this.intermediateStops = intermediateStops;
    }

    public void setLinkedPassengerEmails(List<String> linkedPassengerEmails) {
        this.linkedPassengerEmails = linkedPassengerEmails;
    }

    public void setBabyFriendly(boolean babyFriendly) {
        this.babyFriendly = babyFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


}



