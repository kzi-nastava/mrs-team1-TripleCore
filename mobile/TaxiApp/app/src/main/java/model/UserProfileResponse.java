package model;

public class UserProfileResponse  {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String profileImage;

    public UserProfileResponse() {}

    public UserProfileResponse(Long id, String email, String firstName,
                               String lastName, String address, String phone,
                               String profileImage) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.profileImage = profileImage;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
