package model;

public class RegisterDriverRequest {
    public String firstName;
    public String lastName;
    public String email;
    public String address;
    public String phoneNumber;
    public String profileImage;
    public String vehicleModel;
    public String vehicleType;
    public String brand;
    public String plateNum;
    public int seatNum;
    public boolean babySafe;
    public boolean petSafe;



        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getAddress() {
            return address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public String getVehicleModel() {
            return vehicleModel;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public String getBrand() {
            return brand;
        }

        public String getPlateNum() {
            return plateNum;
        }

        public int getSeatNum() {
            return seatNum;
        }

        public boolean isBabySafe() {
            return babySafe;
        }

        public boolean isPetSafe() {
            return petSafe;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public void setVehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public void setPlateNum(String plateNum) {
            this.plateNum = plateNum;
        }

        public void setSeatNum(int seatNum) {
            this.seatNum = seatNum;
        }

        public void setBabySafe(boolean babySafe) {
            this.babySafe = babySafe;
        }

        public void setPetSafe(boolean petSafe) {
            this.petSafe = petSafe;
        }




}