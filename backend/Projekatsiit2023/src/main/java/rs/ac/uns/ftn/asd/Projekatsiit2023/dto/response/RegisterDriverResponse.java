package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;

public class RegisterDriverResponse  extends  RegisterResponse{
    private String vehicleModel;
    private VehicleType vehicleType;
    private String plateNum;
    private int seatNum;
    private boolean babySafe;
    private boolean petSafe;

    public RegisterDriverResponse(Long id, String email, String firstName,
                                  String lastName, String address, String phoneNumber,
                                  String profilePicture, UserRole role, boolean activated, String message,
                                  String vehicleModel, VehicleType vehicleType,
                                  String plateNum, int seatNum,
                                  boolean babySafe, boolean petSafe) {
        super(id, email, firstName, lastName, address, phoneNumber, profilePicture,  role, activated, message);
        this.vehicleModel = vehicleModel;
        this.vehicleType = vehicleType;
        this.plateNum = plateNum;
        this.seatNum = seatNum;
        this.babySafe = babySafe;
        this.petSafe = petSafe;
    }

    public String getVehicleModel() { return vehicleModel; }
    public VehicleType getVehicleType() { return vehicleType; }
    public String getPlateNum() { return plateNum; }
    public int getSeatNum() { return seatNum; }
    public boolean isBabySafe() { return babySafe; }
    public boolean isPetSafe() { return petSafe; }


}
