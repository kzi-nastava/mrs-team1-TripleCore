package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
@Getter
@Setter
public class UserBlockedResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private boolean blocked;

    public UserBlockedResponse(User user){
        this.id = user.getId();
        this.firstname = user.getFirstName();
        this.lastname = user.getLastName();
        this.email = user.getEmail();
        this.blocked = user.isAccountBlocked();
    }

}
