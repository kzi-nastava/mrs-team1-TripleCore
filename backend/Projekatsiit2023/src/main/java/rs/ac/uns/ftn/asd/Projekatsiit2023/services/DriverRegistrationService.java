package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl.EmailService;

public interface DriverRegistrationService {
    public void  registerDriver(UserRepository userRepository, EmailService emailService);
}
