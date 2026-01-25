package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.vehicle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Location;

@Getter
@Setter
@NoArgsConstructor
public class ActiveRideVehicleDetailsResponse {

    private Long rideId;
    private Long vehicleId;
    private Location vehicleLocation;
    private Long estimatedTime;
    private int estimatedDistance;
}
