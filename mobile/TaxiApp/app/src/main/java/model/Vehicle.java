package model;

import enums.VehicleType;

public class Vehicle {
    private String brand;
    private String model;
    private String plateNumber;
    private int seatNumber;
    private boolean babyFriendly;
    private boolean petFriendly;
    private VehicleType type;


    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }

    public boolean isBabyFriendly() { return babyFriendly; }
    public void setBabyFriendly(boolean babyFriendly) { this.babyFriendly = babyFriendly; }

    public boolean isPetFriendly() { return petFriendly; }
    public void setPetFriendly(boolean petFriendly) { this.petFriendly = petFriendly; }

    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }
}
