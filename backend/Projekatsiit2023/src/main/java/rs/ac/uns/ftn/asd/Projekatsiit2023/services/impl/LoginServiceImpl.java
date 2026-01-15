package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.LoginRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.LoginResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    public LoginServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        // find user by email
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check if account is activated and not blocked
        if (!user.isAccountActivated()) {
            throw new RuntimeException("Account is not activated");
        }

        if (user.isAccountBlocked()) {
            throw new RuntimeException("Account is blocked");
        }

        // check password
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // temporary generate fake JWT token
        String fakeToken = "FAKE-JWT-TOKEN-" + user.getId();

        // if user is driver, set driverAvailable to true
        boolean driverAvailable = user.getRole() == UserRole.DRIVER;

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName(),
                fakeToken,
                driverAvailable
        );
    }

}
