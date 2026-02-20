package model;

public class FavoriteRouteResponse {
    private Long id;
    private String startAddress;
    private double startLat;
    private double startLon;
    private String endAddress;
    private double endLat;
    private double endLon;
    private double estimatedDistanceMeters;
    private Long estimatedDurationSeconds;


    public Long getId() { return id; }
    public String getStartAddress() { return startAddress; }
    public double getStartLat() { return startLat; }
    public double getStartLon() { return startLon; }
    public String getEndAddress() { return endAddress; }
    public double getEndLat() { return endLat; }
    public double getEndLon() { return endLon; }
}
