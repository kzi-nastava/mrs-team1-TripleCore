package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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

}