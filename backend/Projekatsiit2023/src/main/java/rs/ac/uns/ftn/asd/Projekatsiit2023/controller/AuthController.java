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
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.AccountActivationService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.LoginService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RegisterService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final LoginService loginService;
    private final RegisterService registerService;
    private final AccountActivationService accountActivationService;

    public AuthController(LoginService loginService,
                          RegisterService registerService,
                          AccountActivationService accountActivationService) {
        this.loginService = loginService;
        this.registerService = registerService;
        this.accountActivationService = accountActivationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request));
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
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = registerService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam("userId") Long userId) {
        boolean activated = accountActivationService.activateUser(userId);

        if (activated) {
            return ResponseEntity.ok("""
                <html>
                <head>
                    <title>Account Activated</title>
                    <style>
                        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }
                        .success { color: green; }
                        .error { color: red; }
                        .button { 
                            display: inline-block; 
                            padding: 10px 20px; 
                            margin: 10px;
                            background-color: #007bff; 
                            color: white; 
                            text-decoration: none; 
                            border-radius: 5px; 
                        }
                        .container { 
                            max-width: 600px; 
                            margin: 0 auto; 
                            padding: 30px; 
                            border: 1px solid #ddd; 
                            border-radius: 10px;
                            box-shadow: 0 0 10px rgba(0,0,0,0.1);
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1 class="success"> Account Activated Successfully!</h1>
                        <p>Your account has been activated. You can now login to the system.</p>
                        <a href="http://localhost:4200/login" class="button">Go to Login Page</a>
                        <p><small>You can close this window.</small></p>
                    </div>
                </body>
                </html>
                """);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("""
                <html>
                <head>
                    <title>Activation Failed</title>
                    <style>
                        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }
                        .error { color: red; }
                        .button { 
                            display: inline-block; 
                            padding: 10px 20px; 
                            margin: 10px;
                            background-color: #6c757d; 
                            color: white; 
                            text-decoration: none; 
                            border-radius: 5px; 
                        }
                        .container { 
                            max-width: 600px; 
                            margin: 0 auto; 
                            padding: 30px; 
                            border: 1px solid #ddd; 
                            border-radius: 10px;
                            box-shadow: 0 0 10px rgba(0,0,0,0.1);
                        }
                        .warning { 
                            background-color: #fff3cd; 
                            border: 1px solid #ffc107; 
                            padding: 15px; 
                            border-radius: 5px; 
                            margin: 20px 0;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1 class="error">Activation Failed</h1>
                        <div class="warning">
                            <p><strong>Possible reasons:</strong></p>
                            <ul style="text-align: left;">
                                <li>Activation link has expired (24 hour limit)</li>
                                <li>User account not found</li>
                                <li>Account already activated</li>
                            </ul>
                        </div>
                        <p>Please register again to get a new activation link.</p>
                        <a href="http://localhost:4200/register" class="button">Go to Registration Page</a>
                    </div>
                </body>
                </html>
                """);
        }
    }

}