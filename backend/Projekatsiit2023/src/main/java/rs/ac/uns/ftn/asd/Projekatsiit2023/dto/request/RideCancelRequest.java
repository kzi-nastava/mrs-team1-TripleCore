package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request;

import jakarta.validation.constraints.NotNull;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.CancelerType;

public class RideCancelRequest {

    private String reason;

    @NotNull(message = "Canceler type is required (DRIVER or PASSENGER)")
    private CancelerType cancelerType;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public CancelerType getCancelerType() { return cancelerType; }
    public void setCancelerType(CancelerType cancelerType) {
        this.cancelerType = cancelerType;
    }
}