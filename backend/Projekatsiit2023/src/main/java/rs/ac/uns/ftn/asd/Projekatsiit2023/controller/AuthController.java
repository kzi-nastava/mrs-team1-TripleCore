package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.LoginRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RegisterRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.LoginResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RegisterResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if ("driver@example.com".equals(email) && "driver123".equals(password)) {
            LoginResponse response = new LoginResponse(
                    1L,
                    email,
                    UserRole.DRIVER,
                    "Marko",
                    "Markovic",
                    "jwt-token-123",
                    true
            );
            return ResponseEntity.ok(response);
        }

        if ("passenger@example.com".equals(email) && "passenger123".equals(password)) {
            LoginResponse response = new LoginResponse(
                    2L,
                    email,
                    UserRole.PASSENGER,
                    "Ana",
                    "Anic",
                    "jwt-token-456",
                    false
            );
            return ResponseEntity.ok(response);
        }

        if ("admin@example.com".equals(email) && "admin123".equals(password)) {
            LoginResponse response = new LoginResponse(
                    3L,
                    email,
                    UserRole.ADMIN,
                    "Petar",
                    "Petrovic",
                    "jwt-token-789",
                    false
            );
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }

        return ResponseEntity.ok(
                "Password reset link has been sent to: " + email
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword) {

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token is required");
        }

        if (newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest().body("Password must be at least 6 characters long");
        }

        return ResponseEntity.ok("Password has been successfully reset.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam("userId") Long userId) {
        boolean isDriver = userId == 1L;

        if (isDriver) {
            boolean hasActiveRide = Math.random() > 0.5;

            if (hasActiveRide) {
                return ResponseEntity.badRequest()
                        .body("Cannot logout while having an active ride.");
            }

            System.out.println("Driver ID " + userId + " logged out and set to unavailable.");
        }

        return ResponseEntity.ok("User ID " + userId + " logged out successfully.");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        if (emailExists(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        RegisterResponse response = getRegisterResponse(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private static RegisterResponse getRegisterResponse(RegisterRequest request) {
        Long newUserId = 100L;

        return new RegisterResponse(
                newUserId,
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getAddress(),
                request.getPhoneNumber(),
                "default-avatar.png",
                UserRole.PASSENGER,
                false,
                "Registration successful! Check your email for activation link."
        );
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam("token") String token) {
        if (token != null && !token.isEmpty()) {
            return ResponseEntity.ok("Account activated successfully!");
        }
        return ResponseEntity.badRequest().body("Invalid activation token");
    }

    private boolean emailExists(String email) {
        return "existing@example.com".equals(email);
    }
}