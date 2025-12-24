package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import java.time.LocalDateTime;

public class RideStopResponse {
    private boolean success;
    private String message;
    private Double newTotalPrice;
    private Double newDistance;
    private LocalDateTime stopTime;
    private String finalAddress;

    public RideStopResponse(boolean success, String message,
                            Double newTotalPrice, Double newDistance,
                            LocalDateTime stopTime, String finalAddress) {
        this.success = success;
        this.message = message;
        this.newTotalPrice = newTotalPrice;
        this.newDistance = newDistance;
        this.stopTime = stopTime;
        this.finalAddress = finalAddress;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Double getNewTotalPrice() { return newTotalPrice; }
    public Double getNewDistance() { return newDistance; }
    public LocalDateTime getStopTime() { return stopTime; }
    public String getFinalAddress() { return finalAddress; }
}