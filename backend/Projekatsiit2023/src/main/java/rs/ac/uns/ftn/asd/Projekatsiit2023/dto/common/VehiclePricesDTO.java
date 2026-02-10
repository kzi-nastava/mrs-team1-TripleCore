package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.VehicleType;

public class VehiclePricesDTO {
    public double van;
    public double standard;
    public double luxury;

    public double getPriceForType(VehicleType type){
        if(type == VehicleType.VAN) return van;
        else if(type == VehicleType.STANDARD) return standard;
        else if(type == VehicleType.LUXURY) return luxury;
        return 0;
    }
}
