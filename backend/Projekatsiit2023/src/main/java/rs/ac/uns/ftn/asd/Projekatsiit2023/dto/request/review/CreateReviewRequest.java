package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateReviewRequest {
    private Long rideId;
    private Long passengerId;
    private int driverRating;
    private int vehicleRating;
    private String comment;
}

