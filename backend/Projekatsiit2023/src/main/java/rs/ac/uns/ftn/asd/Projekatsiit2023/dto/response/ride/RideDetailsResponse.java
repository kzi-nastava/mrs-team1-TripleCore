package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.ReviewPresentationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RideDetailsResponse {
    // passengers
    private Long id;
    private String ordererName;
    private List<String> linkedPassengers;

    // driver
    private String driverName;
    private String vehicle;

    // route
    private Location startLocation;
    private Location endLocation;
    private List<Location> routeStops;

    // time
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // panic
    private boolean panic;
    private String panicTriggeredBy;
    private LocalDateTime panicTriggeredAt;

    // other info
    private double price;
    private RideStatus status;
    private UserRole cancelledBy;
    private List<ReviewPresentationDTO> reviews;
    private String inconsistencies;
}
