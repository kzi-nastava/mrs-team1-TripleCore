package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;

public class LoginResponse {
    private Long id;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String token; // JWT token - authentication token
    private boolean driverAvailable;

    public LoginResponse(Long id, String email, UserRole role,
                         String firstName, String lastName,
                         String token, boolean driverAvailable) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = token;
        this.driverAvailable = driverAvailable;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getToken() { return token; }
    public boolean isDriverAvailable() { return driverAvailable; }
}