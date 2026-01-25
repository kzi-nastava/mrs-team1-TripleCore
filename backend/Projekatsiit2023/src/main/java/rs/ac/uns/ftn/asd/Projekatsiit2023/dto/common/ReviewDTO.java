package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDTO {
    private Long rideId;
    private Long passengerId;
    private String passengerName;
    private Long driverId;
    private String driverName;
    private int driverRating;
    private int vehicleRating;
    private String comment;
}
