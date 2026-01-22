package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideStopRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideStopResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;

public interface RideStopService {

    RideStopResponse stopRide(Long rideId, RideStopRequest request);

    double calculateNewDistance(Ride ride, double newLat, double newLng);

    double recalculatePrice(Ride ride, double originalDistance, double newDistance);
}
