package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.DriverProfileResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.UserProfileResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.DriverProfileChangeRequest;

public interface UserProfileService {
    UserProfileResponse getUserProfile(Long userId);
    DriverProfileResponse getDriverProfile(Long driverId);
    void updateProfile(Long userId, UpdateUserProfileRequest request);
    UserRole getCurrentUserRole(Long userId);
    DriverProfileChangeRequest createProfileChangeRequest(Long driverId, UpdateUserProfileRequest request);
}
