package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideCancelRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideCancelResponse;

public interface RideCancelService {
    RideCancelResponse cancelRide(Long rideId, RideCancelRequest request);
}
