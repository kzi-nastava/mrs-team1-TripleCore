package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

public class UserProfileResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String profilePicture;

    public UserProfileResponse() {}

    public UserProfileResponse(Long id, String email, String firstName,
                               String lastName, String address, String phoneNumber,
                               String profilePicture) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getProfilePicture() { return profilePicture; }
}
