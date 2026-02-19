package model;

import java.util.List;

public class RideResponse {
    private Long id;
    private LocationDTO startLocation;
    private LocationDTO endLocation;
    private List<LocationDTO> routeStops;
    private String status;


    public Long getId() { return id; }
    public LocationDTO getStartLocation() { return startLocation; }
    public LocationDTO getEndLocation() { return endLocation; }
    public List<LocationDTO> getRouteStops() { return routeStops; }
}