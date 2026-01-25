package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.ChangePasswordRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.UserProfileResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.UserProfileService;

import java.util.Locale;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserProfileService userProfileService;

    public ProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


   @GetMapping("/user")
    public ResponseEntity<UserProfileResponse> getUserProfile(@RequestParam Long userId) {
        UserProfileResponse profile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/driver")
    public ResponseEntity<DriverProfileResponse> getDriverProfile(@RequestParam Long driverId) {
        DriverProfileResponse profile = userProfileService.getDriverProfile(driverId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestParam Long userId, @Valid @RequestBody UpdateUserProfileRequest request){
        try{
            userProfileService.updateProfile(userId, request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
//        UserRole role = getCurrentUserRole();
//
//        if (role == UserRole.DRIVER) {
//
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//
//
//        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        System.out.println("Lozinka je promjenjena: " + request.getNewPassword());

        return ResponseEntity.ok().build();
    }


}
