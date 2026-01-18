package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideCancelRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideCancelResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.CancelerType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideCancelService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class RideCancelServiceImpl implements RideCancelService {

    private final RideRepository rideRepository;

    public RideCancelServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public RideCancelResponse cancelRide(Long rideId, RideCancelRequest request) {
        // find ride by id
        Optional<Ride> rideOpt = rideRepository.findById(rideId);

        if (rideOpt.isEmpty()) {
            throw new RuntimeException("Ride with ID " + rideId + " not found");
        }

        Ride ride = rideOpt.get();

        CancelerType cancelerType = request.getCancelerType();
        String reason = request.getReason();

        if (cancelerType == CancelerType.DRIVER) {
            if (reason == null || reason.trim().isEmpty()) {
                throw new RuntimeException("Driver must provide a cancellation reason");
            }
            ride.setStatus(RideStatus.CANCELLED);
            ride.setCancelledBy(ride.getDriver());
            ride.setInconsistencies(reason);

        } else if (cancelerType == CancelerType.PASSENGER) {
            LocalDateTime now = LocalDateTime.now();
            if (ride.getStartTime().minusMinutes(10).isBefore(now)) {
                throw new RuntimeException("Passenger can only cancel 10 minutes before ride start");
            }
            ride.setStatus(RideStatus.CANCELLED);
            ride.setCancelledBy(ride.getOrderer());
            ride.setInconsistencies(reason);
        }

        rideRepository.save(ride);

        return new RideCancelResponse(true, cancelerType, reason);
    }
}
