package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RegisterDriverRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.AccountActivationService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl.DriverRegistrationServiceImpl;

@RestController
@RequestMapping("/api/driver-auth")
public class DriverAuthController {
    private final AccountActivationService accountActivationService;
    private final DriverRegistrationServiceImpl driverRegistrationService;

    public DriverAuthController(AccountActivationService accountActivationService,
                                DriverRegistrationServiceImpl driverRegistrationService) {
        this.accountActivationService = accountActivationService;
        this.driverRegistrationService = driverRegistrationService;
    }

    @PostMapping("/register-driver")
    public ResponseEntity<String> registerDriver(@RequestBody @Valid RegisterDriverRequest request) {
        try {
            driverRegistrationService.registerDriver(request);
            return ResponseEntity.ok("Driver registered successfully! Activation email sent.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }


    @GetMapping("/activate-driver")
    public ResponseEntity<String> showActivationForm(@RequestParam("userId") Long userId) {
        if (!accountActivationService.canActivate(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("<h2>Activation Failed. Link expired or already activated.</h2>");
        }


        String html = """
            <html>
            <head>
                <title>Activate Account</title>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f5f5f5; text-align: center; padding: 50px; }
                    .container { 
                        max-width: 500px; 
                        margin: 0 auto; 
                        padding: 30px; 
                        background-color: white; 
                        border: 1px solid #ddd; 
                        border-radius: 10px; 
                        box-shadow: 0 0 10px rgba(0,0,0,0.1);
                    }
                    input { display: block; width: 90%%; margin: 10px auto; padding: 10px; border-radius: 5px; border: 1px solid #ccc; }
                    button { padding: 10px 20px; border-radius: 5px; border: none; background-color: #007bff; color: white; cursor: pointer; }
                    h1 { color: #333; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>Activate Your Account</h1>
                    <form method="POST" action="/api/driver-auth/activate-driver">
                        <input type="hidden" name="userId" value="%d">
                        <input type="password" name="password" placeholder="Password" required minlength="6">
                        <input type="password" name="confirmPassword" placeholder="Confirm Password" required minlength="6">
                        <button type="submit">Activate Account</button>
                    </form>
                </div>
            </body>
            </html>
            """.formatted(userId);

        return ResponseEntity.ok(html);
    }

    @PostMapping("/activate-driver")
    public ResponseEntity<String> activateDriver(
            @RequestParam("userId") Long userId,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            String html = """
                    <html>
                    <head><title>Activation Error</title></head>
                    <body style="font-family: Arial, sans-serif; text-align: center; padding: 50px;">
                        <h2 style="color:red;">Passwords do not match</h2>
                        <a href="/api/driver-auth/activate-driver?userId=%d">Try Again</a>
                    </body>
                    </html>
                    """.formatted(userId);
            return ResponseEntity.badRequest().body(html);
        }

        if (password.length() < 6) {
            return ResponseEntity.badRequest().body("<h2 style='color:red;'>Password must be at least 6 characters</h2>");
        }

        boolean activated = accountActivationService.activateUserWithPassword(userId, password);

        if (activated) {
            String html = """
                    <html>
                    <head><title>Account Activated</title></head>
                    <body style="font-family: Arial, sans-serif; text-align: center; padding: 50px;">
                        <h2 style="color:green;">Account Activated Successfully!</h2>
                        <a href="http://localhost:4200/login">Go to Login Page</a>
                    </body>
                    </html>
                    """;
            return ResponseEntity.ok(html);
        } else {
            String html = """
                    <html>
                    <head><title>Activation Failed</title></head>
                    <body style="font-family: Arial, sans-serif; text-align: center; padding: 50px;">
                        <h2 style="color:red;">Activation Failed. Link expired or already activated.</h2>
                        <a href="http://localhost:4200/register">Go to Registration Page</a>
                    </body>
                    </html>
                    """;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(html);
        }
    }



}
