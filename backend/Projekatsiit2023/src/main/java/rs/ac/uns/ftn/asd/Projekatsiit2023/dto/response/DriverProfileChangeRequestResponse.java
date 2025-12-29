package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.DriverUpdateRequestStatus;

public class DriverProfileChangeRequestResponse {
    private Long requestId;
    private Long driverId;
    private UpdateUserProfileRequest requestedChanges;
    @Setter
    private DriverUpdateRequestStatus status;

    public DriverProfileChangeRequestResponse(Long requestId, Long driverId,
                                              UpdateUserProfileRequest requestedChanges,
                                              DriverUpdateRequestStatus status) {
        this.requestId = requestId;
        this.driverId = driverId;
        this.requestedChanges = requestedChanges;
        this.status = status;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public UpdateUserProfileRequest getRequestedChanges() {
        return requestedChanges;
    }

    public DriverUpdateRequestStatus getStatus() {
        return status;
    }


}
