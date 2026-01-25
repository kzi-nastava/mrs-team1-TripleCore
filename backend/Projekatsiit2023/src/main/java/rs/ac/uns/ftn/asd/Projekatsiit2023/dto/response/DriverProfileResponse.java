package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import lombok.Getter;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Vehicle;

public class DriverProfileResponse extends UserProfileResponse{

    private double workingHoursToday;
    private Vehicle vehicle;

    public DriverProfileResponse(Long id, String email, String firstName,
                                 String lastName, String address, String phone,
                                 String profileImg, double workingHoursToday, Vehicle vehicle) {
        super(id, email, firstName, lastName, address, phone, profileImg);
        this.workingHoursToday = workingHoursToday;
        this.vehicle = vehicle;
    }

    public double getWorkingHoursToday() { return workingHoursToday; }
    public Vehicle getVehicle () { return vehicle; }



}
