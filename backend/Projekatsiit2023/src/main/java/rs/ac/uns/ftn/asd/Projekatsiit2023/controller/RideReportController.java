package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideReportResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideReportSummaryResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.UserReportResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RideReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class RideReportController {
    private final RideReportService rideReportService;
    private final UserRepository userRepository;

    public RideReportController(RideReportService rideReportService, UserRepository userRepository) {
        this.rideReportService = rideReportService;
        this.userRepository = userRepository;
    }

    // dnevni podaci za graf
    @GetMapping("/user/{userId}")
    public List<RideReportResponse> getDailyReport(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return rideReportService.getRideReportForUser(user, startDate, endDate);
    }

    // summary (total + average)
    @GetMapping("/user/{userId}/summary")
    public RideReportSummaryResponse getSummaryReport(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return rideReportService.getRideReportSummary(user, startDate, endDate);
    }

    @GetMapping("/users")
    public List<UserReportResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new UserReportResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName() + " " + user.getLastName()
                ))
                .toList();

    }


    @GetMapping("/all")
    public List<RideReportResponse> getReportForAllUsers(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        return rideReportService.getRideReportForAllUsers(startDate, endDate);

    }

}
