package model;

public class DriverProfileResponse  extends  UserProfileResponse {
    private double workingHoursToday;
    private Vehicle vehicle;

    public double getWorkingHoursToday() { return workingHoursToday; }
    public Vehicle getVehicle() { return vehicle; }

    public void setWorkingHoursToday(double workingHoursToday) { this.workingHoursToday = workingHoursToday; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
}
