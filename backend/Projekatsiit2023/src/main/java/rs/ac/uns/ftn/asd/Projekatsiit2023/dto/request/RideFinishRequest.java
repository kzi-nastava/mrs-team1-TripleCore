package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request;

import jakarta.validation.constraints.NotNull;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.LocationDTO;

import java.time.LocalDateTime;

public class RideFinishRequest {

    @NotNull(message = "End location is required")
    private LocationDTO endLocation;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    public RideFinishRequest() {
    }

    public LocationDTO getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LocationDTO endLocation) {
        this.endLocation = endLocation;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
