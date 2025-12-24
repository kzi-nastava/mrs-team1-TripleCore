package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.CancelerType;

public class RideCancelResponse {
    private boolean success = true;
    private CancelerType cancelledBy;
    private String reason;

    public RideCancelResponse(boolean success, CancelerType cancelledBy, String reason) {
        this.success = success;
        this.cancelledBy = cancelledBy;
        this.reason = reason;
    }

    public boolean isSuccess() { return success; }
    public CancelerType getCancelledBy() { return cancelledBy; }
    public String getReason() { return reason; }
}