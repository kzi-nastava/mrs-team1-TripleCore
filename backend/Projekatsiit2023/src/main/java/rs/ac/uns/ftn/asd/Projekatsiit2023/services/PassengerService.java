package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.PassengerRepository;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository pr){
        this.passengerRepository = pr;
    }

    public Passenger getPassengerById(Long id){
        return passengerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Passenger with id: " + id + " not found"));
    }
}
