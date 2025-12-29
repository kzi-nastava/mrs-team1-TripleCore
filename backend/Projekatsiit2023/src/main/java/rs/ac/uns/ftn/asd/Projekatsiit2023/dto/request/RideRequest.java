package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;

import java.time.LocalDateTime;
import java.util.List;

public class RideRequest {

    @NotBlank(message = "Start location is required")
    private String startLocation;

    @NotBlank(message = "End location is required")
    private String endLocation;

    private List<String> stops;

    private List<String> linkedPassengersEmails;

    private VehicleType vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    @Positive(message = "Distance must be positive")
    private double distanceInKm;

    @FutureOrPresent(message = "Scheduled time must be in the future")
    private LocalDateTime scheduledTime;

    public String getStartLocation() {
        return startLocation;
    }
    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public List<String> getStops() {
        return stops;
    }

    public void setStops(List<String> stops) {
        this.stops = stops;
    }

    public List<String> getLinkedPassengersEmails() {
        return linkedPassengersEmails;
    }

    public void setLinkedPassengersEmails(List<String> linkedPassengersEmails) {
        this.linkedPassengersEmails = linkedPassengersEmails;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public double getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(double distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }


}
