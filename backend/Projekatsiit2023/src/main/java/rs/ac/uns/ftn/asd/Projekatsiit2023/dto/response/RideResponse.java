package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;

import java.time.LocalDateTime;

import static rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType.*;

public class RideResponse {
    private Long rideId;
    private RideStatus status;
    private double price;
    private Long driverId;
    private String message;

    public RideResponse(Long rideId, RideStatus status, double price, Long driverId, String message) {
        this.rideId = rideId;
        this.status = status;
        this.price = price;
        this.driverId = driverId;
        this.message = message;
    }

    public Long getRideId() {
        return rideId;
    }

    public RideStatus getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getMessage() {
        return message;
    }


}
