package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.CancelerType;

@Setter
@Getter
public class RideCancelRequest {

    private String reason;

    @NotNull(message = "Canceler type is required (DRIVER or PASSENGER)")
    private CancelerType cancelerType;

}