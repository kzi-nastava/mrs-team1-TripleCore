package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common;

public class ReviewDTO {
    private Long passengerId;
    private Long driverId;
    private int driverRating;
    private int vehicleRating;
    private String comment;

    public ReviewDTO() {
    }

    public ReviewDTO(Long passengerId, Long driverId, int driverRating, int vehicleRating, String comment) {
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.driverRating = driverRating;
        this.vehicleRating = vehicleRating;
        this.comment = comment;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public int getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(int driverRating) {
        this.driverRating = driverRating;
    }

    public int getVehicleRating() {
        return vehicleRating;
    }

    public void setVehicleRating(int vehicleRating) {
        this.vehicleRating = vehicleRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
