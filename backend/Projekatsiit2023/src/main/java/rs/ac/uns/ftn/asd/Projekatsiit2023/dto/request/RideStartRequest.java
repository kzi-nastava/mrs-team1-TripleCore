package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request;

import jakarta.validation.constraints.NotNull;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.LocationDTO;

import java.time.LocalDateTime;

public class RideStartRequest {
    @NotNull(message = "Start location is required")
    private LocationDTO startLocation;
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    public LocationDTO getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(double latitude, double longitude) {
        this.startLocation = new LocationDTO(latitude, longitude);
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }
}
