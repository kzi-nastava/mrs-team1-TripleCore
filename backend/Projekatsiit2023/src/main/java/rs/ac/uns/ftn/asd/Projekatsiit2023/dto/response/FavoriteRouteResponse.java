package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

public class FavoriteRouteResponse {
    private Long id;

    private String startAddress;
    double startLat;
    double startLon;

    private String endAddress;
    double endLat;
    double endLon;

    double estimatedDistanceMeters;
    Long estimatedDurationSeconds;

    public FavoriteRouteResponse(Long id, String startAddress, double startLat, double startLon, String endAddress, double endLat, double endLon, double estimatedDistanceMeters, Long estimatedDurationSeconds) {
        this.id = id;
        this.startAddress = startAddress;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endAddress = endAddress;
        this.endLat = endLat;
        this.endLon = endLon;
        this.estimatedDistanceMeters = estimatedDistanceMeters;
        this.estimatedDurationSeconds = estimatedDurationSeconds;

    }

    public Long getId() {
        return id;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public double getStartLat() {
        return startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public double getEndLat() {
        return endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public double getEstimatedDistanceMeters() {
        return estimatedDistanceMeters;
    }

    public Long getEstimatedDurationSeconds() {
        return estimatedDurationSeconds;
    }





}

