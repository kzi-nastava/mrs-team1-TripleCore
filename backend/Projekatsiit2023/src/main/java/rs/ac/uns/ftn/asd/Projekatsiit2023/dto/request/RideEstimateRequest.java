package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request;

import jakarta.validation.constraints.NotBlank;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;

public class RideEstimateRequest {
    @NotBlank(message = "Start address is required")
    private String startAddress;

    @NotBlank(message = "End address is required")
    private String endAddress;

    private VehicleType vehicleType = VehicleType.STANDARD;

    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }

    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }

    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
}