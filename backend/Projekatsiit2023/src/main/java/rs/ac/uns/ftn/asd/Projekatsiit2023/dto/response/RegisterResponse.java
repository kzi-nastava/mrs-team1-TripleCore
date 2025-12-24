package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;

public class RegisterResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String profilePicture;
    private UserRole role;
    private boolean activated;     // false = waiting for activation
    private String message;

    public RegisterResponse(Long id, String email, String firstName,
                            String lastName, String address, String phoneNumber,
                            String profilePicture, UserRole role, boolean activated, String message) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.role = role;
        this.activated = activated;
        this.message = message;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getProfilePicture() { return profilePicture; }
    public UserRole getRole() { return role; }
    public boolean isActivated() { return activated; }
    public String getMessage() { return message; }
}