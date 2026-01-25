package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import lombok.Getter;

@Getter
public class UserProfileResponse {
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

}
