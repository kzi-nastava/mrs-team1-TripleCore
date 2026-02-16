package rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RideReportSummaryResponse {
    private double totalDistance;
    private double totalPrice;
    private int totalRides;
    private double averageDistance;
    private double averagePrice;
}
