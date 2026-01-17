package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import lombok.Getter;

@Getter
public class RideEstimateResponse {
    private Integer estimatedTime; // minutes
    private Double distance; // kilometers
    private String routeCoordinates; // "lat,lng;lat,lng"
    private String message;

    public RideEstimateResponse(Integer estimatedTime,
                                Double distance, String routeCoordinates, String message) {
        this.estimatedTime = estimatedTime;
        this.distance = distance;
        this.routeCoordinates = routeCoordinates;
        this.message = message;
    }

}