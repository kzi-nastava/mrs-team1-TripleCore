package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RideReportResponse {
    private LocalDate date;
    private int rideCount;
    private double totalDistance;
    private double totalPrice;
}
