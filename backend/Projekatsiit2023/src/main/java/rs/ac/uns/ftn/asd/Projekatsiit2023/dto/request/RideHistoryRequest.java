package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideSortBy;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.SortOrder;
import java.time.LocalDate;

public class RideHistoryRequest {
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private RideSortBy sortBy = RideSortBy.DATE;
    private SortOrder sortOrder = SortOrder.DESC;
    private Integer page = 0;   // Default to first page
    private Integer size = 10;  // Default page size

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public RideSortBy getSortBy() { return sortBy; }
    public void setSortBy(RideSortBy sortBy) { this.sortBy = sortBy; }

    public SortOrder getSortOrder() { return sortOrder; }
    public void setSortOrder(SortOrder sortOrder) { this.sortOrder = sortOrder; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}