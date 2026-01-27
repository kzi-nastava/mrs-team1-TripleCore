package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

public class DriverProfileSnapshot {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String profileImage;


    public DriverProfileSnapshot(
            String firstName,
            String lastName,
            String email,
            String address,
            String phone,
            String profileImage
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.profileImage = profileImage;
    }


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

    public String getPhone() {
        return phone;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
