package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.Location;

public class ActiveRideVehicleDetailsResponse {
    private Location location;
    private long estimatedTime;

    public ActiveRideVehicleDetailsResponse(double latitude, double longitude, long estimatedTime){
        this.location = new Location(latitude, longitude);
        this.estimatedTime = estimatedTime;
    }

    public double getLatitutde(){
        return this.location.getLatitude();
    }

    public double getLongitude(){
        return  this.location.getLongitude();
    }

    public long getEstimatedTime() {
        return estimatedTime;
    }
}
