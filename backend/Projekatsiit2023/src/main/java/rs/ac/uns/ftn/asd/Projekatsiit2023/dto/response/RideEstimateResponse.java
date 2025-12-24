package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

public class RideEstimateResponse {
    private Double estimatedPrice;
    private Integer estimatedTime; // minutes
    private Double distance; // kilometers
    private String routeCoordinates; // "lat,lng;lat,lng"
    private String message;

    public RideEstimateResponse(Double estimatedPrice, Integer estimatedTime,
                                Double distance, String routeCoordinates, String message) {
        this.estimatedPrice = estimatedPrice;
        this.estimatedTime = estimatedTime;
        this.distance = distance;
        this.routeCoordinates = routeCoordinates;
        this.message = message;
    }

    public Double getEstimatedPrice() { return estimatedPrice; }
    public Integer getEstimatedTime() { return estimatedTime; }
    public Double getDistance() { return distance; }
    public String getRouteCoordinates() { return routeCoordinates; }
    public String getMessage() { return message; }
}