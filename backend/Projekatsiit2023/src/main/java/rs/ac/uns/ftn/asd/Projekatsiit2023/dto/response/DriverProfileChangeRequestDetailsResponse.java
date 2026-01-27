package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.DriverUpdateRequestStatus;

import java.time.LocalDateTime;

public class DriverProfileChangeRequestDetailsResponse {
    private Long requestId;
    private DriverProfileSnapshot currentProfile;
    private DriverProfileSnapshot requestedProfile;
    private DriverUpdateRequestStatus status;
    private LocalDateTime statusUpdatedAt;

    public DriverProfileChangeRequestDetailsResponse(Long requestId,
                                                     DriverProfileSnapshot currentProfile,
                                                     DriverProfileSnapshot requestedProfile,
                                                     DriverUpdateRequestStatus status,
                                                     LocalDateTime statusUpdatedAt) {
        this.requestId = requestId;
        this.currentProfile = currentProfile;
        this.requestedProfile = requestedProfile;
        this.status = status;
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public Long getRequestId() {
        return requestId;
    }

    public DriverProfileSnapshot getCurrentProfile() {
        return currentProfile;
    }

    public DriverProfileSnapshot getRequestedProfile() {
        return requestedProfile;
    }
    public DriverUpdateRequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getStatusUpdatedAt() {
        return statusUpdatedAt;
    }



}
