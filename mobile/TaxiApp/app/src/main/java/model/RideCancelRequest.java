package model;

public class RideCancelRequest {
    private String reason;
    private String cancelerType; // "DRIVER" or "PASSENGER"

    public RideCancelRequest(String reason, String cancelerType) {
        this.reason = reason;
        this.cancelerType = cancelerType;
    }

    // Getters and setters
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCancelerType() {
        return cancelerType;
    }

    public void setCancelerType(String cancelerType) {
        this.cancelerType = cancelerType;
    }
}