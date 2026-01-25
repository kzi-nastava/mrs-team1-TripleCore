package model;

public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private String address;
    private String phoneNumber;
    private String profileImage;
    private String role;

    public RegisterRequest(String firstName,
                           String lastName,
                           String email,
                           String password,
                           String confirmPassword,
                           String address,
                           String phoneNumber,
                           String profileImage,
                           String role) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.role = role;
    }
}
