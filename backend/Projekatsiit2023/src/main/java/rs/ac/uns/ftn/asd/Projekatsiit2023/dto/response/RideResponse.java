package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;

import java.time.LocalDateTime;
import java.util.List;

import static rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType.*;

public class RideResponse {
    @NotNull(message = "Ride ID cannot be null")
    private Long rideId;

    @NotNull(message = "Estimated end time cannot be null")
    private LocalDateTime estimatedEndTime;

    @NotNull(message = "Vehicle ID cannot be null")
    private Long vehicleId;

    @NotBlank(message = "Driver name cannot be blank")
    private String driverName;

    @NotNull(message = "Route points cannot be null")
    private List<@NotNull Location> routePoints;

    @NotNull(message = "Ride status cannot be null")
    private RideStatus status;


    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public LocalDateTime getEstimatedEndTime() { return estimatedEndTime; }
    public void setEstimatedEndTime(LocalDateTime estimatedEndTime) { this.estimatedEndTime = estimatedEndTime; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public List<Location> getRoutePoints() { return routePoints; }
    public void setRoutePoints(List<Location> routePoints) { this.routePoints = routePoints; }

    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }

    public String toString() {
        return "RideResponse{" +
                "rideId=" + rideId +
                ", estimatedEndTime=" + estimatedEndTime +
                ", vehicleId=" + vehicleId +
                ", driverName='" + driverName + '\'' +
                ", routePoints=" + routePoints +
                ", status=" + status +
                '}';
    }


}
