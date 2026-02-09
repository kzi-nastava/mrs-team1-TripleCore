package model;

public class DriverProfileChangeRequest {
    private Long id;
    private Long driverId;
    private String newFirstName;
    private String newLastName;
    private String newAddress;
    private String newPhone;
    private String newProfileImage;


    public DriverProfileChangeRequest() {}

    public DriverProfileChangeRequest(Long id, Long driverId, String newFirstName,
                                      String newLastName, String newAddress, String newPhone,
                                      String newProfileImage) {
        this.id = id;
        this.driverId = driverId;
        this.newFirstName = newFirstName;
        this.newLastName = newLastName;
        this.newAddress = newAddress;
        this.newPhone = newPhone;
        this.newProfileImage = newProfileImage;
    }

    public Long getId() {
        return id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public String getNewProfileImage() {
        return newProfileImage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    public void setNewProfileImage(String newProfileImage) {
        this.newProfileImage = newProfileImage;
    }




}
