package model;

public class RegisterResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String profileImage;
    private String role;
    private boolean activated;
    private String message;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getRole() {
        return role;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getMessage() {
        return message;
    }
}
