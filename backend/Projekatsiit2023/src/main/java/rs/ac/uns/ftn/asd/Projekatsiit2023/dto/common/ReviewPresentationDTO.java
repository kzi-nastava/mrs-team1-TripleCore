package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewPresentationDTO {
    private String passenger;
    private String driver;
    private int driverRating;
    private int vehicleRating;
    private String comment;
}
