package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request;

import jakarta.validation.constraints.*;
import lombok.Value;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;

public class RegisterDriverRequest extends RegisterRequest {

    @NotBlank(message = "Vehicle model is required")
    private String vehicleModel;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotBlank(message = "Plate number is required")
    private String plateNum;

    @Min(value = 1, message = "Seat number must be at least 1")
    private int seatNum;

    private boolean babySafe;

    private boolean petSafe;

    private UserRole role = UserRole.DRIVER;


    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }

    public String getPlateNum() { return plateNum; }
    public void setPlateNum(String plateNum) { this.plateNum = plateNum; }

    public int getSeatNum() { return seatNum; }
    public void setSeatNum(int seatNum) { this.seatNum = seatNum; }

    public boolean isBabySafe() { return babySafe; }
    public void setBabySafe(boolean babySafe) { this.babySafe = babySafe; }

    public boolean isPetSafe() { return petSafe; }
    public void setPetSafe(boolean petSafe) { this.petSafe = petSafe; }

}
