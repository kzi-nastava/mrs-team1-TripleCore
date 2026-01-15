package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RideDetailsResponse {
    // passengers
    private String ordererName;
    private List<String> linkedPassengers;

    // driver
    private String driverName;
    private String vehicle;
    private double driverRating;

    // route
    private Location startLocation;
    private Location endLocation;
    private List<Location> routeStops;

    // time
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    //


}
