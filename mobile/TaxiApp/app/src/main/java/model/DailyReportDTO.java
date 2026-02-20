package model;

public class DailyReportDTO {
    private String date;
    private int rideCount;
    private double totalPrice;
    private double totalDistance;

    public DailyReportDTO() {}

    public String getDate() { return date; }
    public int getRideCount() { return rideCount; }
    public double getTotalPrice() { return totalPrice; }
    public double getTotalDistance() { return totalDistance; }

    public void setDate(String date) { this.date = date; }
    public void setRideCount(int rideCount) { this.rideCount = rideCount; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }



}
