package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.ChangePasswordRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.UserProfileResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;

import java.util.Locale;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private UserRole getCurrentUserRole(){
        return UserRole.DRIVER;
    }

    @GetMapping
    public ResponseEntity<?> getProfile() {

        UserRole role = getCurrentUserRole();

        if (role == UserRole.DRIVER) {

            DriverProfileResponse response = new DriverProfileResponse(
                    41L,
                    "driver@example.com",
                    "Boban",
                    "Rajovic",
                    "Bulevar Kralja Aleksandra 10, Beograd",
                    "+381641112233",
                    "https://example.com/profile-picture.jpg",
                    6.0,
                    "BMW X3",
                    "BG-456",
                    5,
                    VehicleType.STANDARD,
                    true,
                    true
            );

            return ResponseEntity.ok(response);
        } else {
            UserProfileResponse response = new UserProfileResponse(
                    42L,
                    "driver2#example.com",
                    "Mitar",
                    "Miric",
                    "Dobriccina 12, Beograd",
                    "+381659998877",
                    "https://example.com/profile-picture2.jpg"
            );

            return ResponseEntity.ok(response);
        }
    }

    @PutMapping
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateUserProfileRequest request){
        UserRole role = getCurrentUserRole();

        if (role == UserRole.DRIVER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        System.out.println("Profil je azuriran: " + request);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        UserRole role = getCurrentUserRole();

        if (role == UserRole.DRIVER) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("Lozinka je promjenjena: " + request.getNewPassword());

        return ResponseEntity.ok().build();
    }


}
