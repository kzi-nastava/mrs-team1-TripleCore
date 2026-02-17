package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideReportResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideReportSummaryResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;

import java.time.LocalDate;
import java.util.List;

public interface RideReportService {
    public List<RideReportResponse> getRideReportForUser(User user, LocalDate startDate, LocalDate endDate);
    public RideReportSummaryResponse getRideReportSummary(User user, LocalDate startDate, LocalDate endDate);
    List<RideReportResponse> getRideReportForAllUsers(LocalDate startDate, LocalDate endDate);
}
