package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.DriverUpdateRequestStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.DriverProfileChangeRequest;

import java.time.LocalDateTime;

public class DriverProfileChangeRequestResponse {
    private Long id;
    private String email;
    private LocalDateTime createdAt;
    private String status;

    public DriverProfileChangeRequestResponse(DriverProfileChangeRequest req) {
        this.id = req.getId();
        this.email = req.getEmail();
        this.createdAt = req.getCreatedAt();
        this.status = req.getStatus().toString();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

}
