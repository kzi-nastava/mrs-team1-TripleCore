package model;

import enums.UserRole;

public class LoginResponse {
    private Long id;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String token; // JWT
    private boolean driverAvailable;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public boolean isDriverAvailable() { return driverAvailable; }
    public void setDriverAvailable(boolean driverAvailable) { this.driverAvailable = driverAvailable; }
}
