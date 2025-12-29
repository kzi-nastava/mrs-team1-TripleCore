package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;

public class DriverProfileResponse extends UserProfileResponse{
    private double activeHoursLast24h;

    private String vehicleModel;
    private String plateNumber;
    private int seatNumber;
    private VehicleType vehicleType;
    private boolean babySafe;
    private boolean petSafe;

    public DriverProfileResponse(Long id, String email, String firstName,
                                 String lastName, String address, String phoneNumber,
                                 String profilePicture, double activeHoursLast24h,
                                 String vehicleModel, String plateNumber,
                                 int seatNumber, VehicleType vehicleType,
                                 boolean babySafe, boolean petSafe) {
        super(id, email, firstName, lastName, address, phoneNumber, profilePicture);
        this.activeHoursLast24h = activeHoursLast24h;
        this.vehicleModel = vehicleModel;
        this.plateNumber = plateNumber;
        this.seatNumber = seatNumber;
        this.vehicleType = vehicleType;
        this.babySafe = babySafe;
        this.petSafe = petSafe;
    }

    public double getActiveHoursLast24h() { return activeHoursLast24h; }
    public String getVehicleModel() { return vehicleModel; }
    public String getPlateNumber() { return plateNumber; }
    public int getSeatNumber() { return seatNumber; }
    public VehicleType getVehicleType() { return vehicleType; }
    public boolean isBabySafe() { return babySafe; }
    public boolean isPetSafe() { return petSafe; }



}
