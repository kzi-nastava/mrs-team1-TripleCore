package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import java.util.List;

public class RideHistoryResponse {
    private List<RideHistoryItemResponse> rides;
    //private int currentPage;
    //private int totalPages;
    //private long totalItems;

    public RideHistoryResponse(List<RideHistoryItemResponse> rides) {
        this.rides = rides;
        //this.currentPage = currentPage;
        //this.totalPages = totalPages;
        //this.totalItems = totalItems;
    }

    public List<RideHistoryItemResponse> getRides() { return rides; }
    //public int getCurrentPage() { return currentPage; }
    //public int getTotalPages() { return totalPages; }
    //public long getTotalItems() { return totalItems; }
}