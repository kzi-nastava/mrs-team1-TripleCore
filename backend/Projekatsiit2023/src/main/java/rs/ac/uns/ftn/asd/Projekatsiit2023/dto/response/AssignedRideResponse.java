package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.RideDTO;

public class AssignedRideResponse {
    private boolean hasAssignedRide;
    private RideDTO ride;

    public AssignedRideResponse() {
    }

    public boolean isHasAssignedRide() {
        return hasAssignedRide;
    }

    public void setHasAssignedRide(boolean hasAssignedRide) {
        this.hasAssignedRide = hasAssignedRide;
    }

    public RideDTO getRide() {
        return ride;
    }

    public void setRide(RideDTO ride) {
        this.ride = ride;
    }
}

