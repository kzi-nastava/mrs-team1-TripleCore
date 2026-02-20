package model;
public class SummaryDTO {
    private double totalDistance;
    private double totalPrice;
    private int totalRides;
    private double averageDistance;
    private double averagePrice;


    public double getTotalDistance() { return totalDistance; }
    public double getTotalPrice() { return totalPrice; }
    public int getTotalRides() { return totalRides; }
    public double getAverageDistance() { return averageDistance; }
    public double getAveragePrice() { return averagePrice; }
}