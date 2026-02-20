package model;

public class UserBlockedResponse {
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private boolean blocked;


    public long getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }
}
