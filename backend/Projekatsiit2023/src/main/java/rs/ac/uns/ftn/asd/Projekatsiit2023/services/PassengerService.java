package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ride.RideDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.PassengerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.RideRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final RideRepository rideRepository;

    public PassengerService(
            PassengerRepository passengerRepository,
            RideRepository rideRepository) {
        this.passengerRepository = passengerRepository;
        this.rideRepository = rideRepository;
    }

    public Passenger getPassengerById(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Passenger with id: " + id + " not found"));
    }

    public List<RideDetailsResponse> getRideHistory(Long passengerId) {
        try {
            List<Ride> rides = rideRepository.findByPassengerId(passengerId);

            return rides.stream()
                    .map(RideService::createRideDetails)
                    .toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}