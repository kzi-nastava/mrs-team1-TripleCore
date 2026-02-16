package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserReportResponse {

    private Long id;
    private String email;
    private String fullName;

}