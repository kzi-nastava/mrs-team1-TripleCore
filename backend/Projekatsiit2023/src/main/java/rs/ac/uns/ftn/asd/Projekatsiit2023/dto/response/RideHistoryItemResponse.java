package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.CancelerType;
import java.time.LocalDateTime;

public class RideHistoryItemResponse {
    private Long id;
    private String route;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String startAddress;
    private String endAddress;
    private boolean cancelled;
    private CancelerType cancelledBy;
    private Double price;
    private boolean panicButtonPressed;

    public RideHistoryItemResponse(Long id, String route, LocalDateTime startTime,
                                  LocalDateTime endTime, String startAddress,
                                  String endAddress, boolean cancelled,
                                  CancelerType cancelledBy, Double price,
                                  boolean panicButtonPressed) {
        this.id = id;
        this.route = route;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.cancelled = cancelled;
        this.cancelledBy = cancelledBy;
        this.price = price;
        this.panicButtonPressed = panicButtonPressed;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }

    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }

    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    public CancelerType getCancelledBy() { return cancelledBy; }
    public void setCancelledBy(CancelerType cancelledBy) { this.cancelledBy = cancelledBy; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public boolean isPanicButtonPressed() { return panicButtonPressed; }
    public void setPanicButtonPressed(boolean panicButtonPressed) {
        this.panicButtonPressed = panicButtonPressed;
    }
}