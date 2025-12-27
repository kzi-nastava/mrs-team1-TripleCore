package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import java.util.List;

public class RideHistoryResponse {
    private List<RideHistoryItemResponse> rides;

    public RideHistoryResponse(List<RideHistoryItemResponse> rides) {
        this.rides = rides;
    }

    public List<RideHistoryItemResponse> getRides() { return rides; }
}