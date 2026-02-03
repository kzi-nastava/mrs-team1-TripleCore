package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RegisterDriverRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.VehicleRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DriverRegistrationServiceImpl {
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final EmailService emailService;

    public DriverRegistrationServiceImpl(UserRepository userRepository, VehicleRepository vehicleRepository, EmailService emailService){
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void registerDriver(RegisterDriverRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(request.getVehicleModel());
        vehicle.setType(request.getVehicleType());
        vehicle.setBrand(request.getBrand());
        vehicle.setPlateNumber(request.getPlateNum());
        vehicle.setSeatNumber(request.getSeatNum());
        vehicle.setBabyFriendly(request.isBabySafe());
        vehicle.setPetFriendly(request.isPetSafe());

        vehicleRepository.save(vehicle);


        Driver driver = new Driver();

        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setEmail(request.getEmail());
        driver.setAddress(request.getAddress());
        driver.setPhone(request.getPhoneNumber());
        driver.setRole(UserRole.DRIVER);

        String tempPassword = UUID.randomUUID().toString();
        driver.setPassword(tempPassword);
        driver.setAccountActivated(false);
        driver.setCreatedAt(LocalDateTime.now());

        driver.setCurrentlyWorking(false);
        driver.setWorkingHoursToday(0);
        driver.setAvailable(false);

        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            driver.setProfileImage(request.getProfileImage());
        } else {
            driver.setProfileImage("/icons/profile.png"); // Default URL
        }

        driver.setVehicle(vehicle);

        User savedUser = userRepository.save(driver);

        String activationLink = generateActivationLink(savedUser.getId());


        emailService.sendActivationEmail(savedUser.getEmail(), activationLink);



    }


    public String generateActivationLink(Long userId) {
        return "http://localhost:8080/api/driver-auth/activate-driver?userId=" + userId;
    }





}
