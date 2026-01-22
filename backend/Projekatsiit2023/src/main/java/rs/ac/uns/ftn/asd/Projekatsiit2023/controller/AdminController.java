package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RegisterDriverRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.UpdateUserProfileRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Panic;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.PanicService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PanicService panicService;

    public AdminController(PanicService panicService) {
        this.panicService = panicService;
    }

    @GetMapping("/panics")
    public List<Panic> getAllPanics() {
        return panicService.getAllPanics();
    }

    @GetMapping("/panics/active")
    public List<Panic> getActivePanics() {
        return panicService.getActivePanics();
    }

    @GetMapping("/panics/resolved")
    public List<Panic> getResolvedPanics() {
        return panicService.getResolvedPanics();
    }

    @PutMapping("/panics/{id}/resolve")
    public void resolvePanic(@PathVariable Long id) {
        panicService.markAsResolved(id);
    }

    @GetMapping("/rides")
    public ResponseEntity<RideHistoryResponse> getRides(
            @RequestParam(name = "userId", required = false) Long userId,

            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(name = "sortBy", required = false) RideSortBy sortBy,
            @RequestParam(name = "sortOrder", required = false) SortOrder sortOrder
    ) {

        List<RideHistoryItemResponse> filteredRides = generateMockRides();

        if (userId != null) {
            filteredRides = filterByUserId(filteredRides, userId);
        }

        if (startDate != null) {
            filteredRides = filterByStartDate(filteredRides, startDate);
        }

        if (endDate != null) {
            filteredRides = filterByEndDate(filteredRides, endDate);
        }

        filteredRides = sortRides(filteredRides, sortBy, sortOrder);

        return ResponseEntity.ok(new RideHistoryResponse(filteredRides));
    }

    private List<RideHistoryItemResponse> filterByUserId(
            List<RideHistoryItemResponse> rides, Long userId) {
        if (userId == 101) {
            return rides.stream().filter(r -> r.getId() == 1 || r.getId() == 2).toList();
        } else if (userId == 202) {
            return rides.stream().filter(r -> r.getId() == 3 || r.getId() == 4).toList();
        } else if (userId == 303) {
            return rides.stream().filter(r -> r.getId() == 5).toList();
        }
        return List.of();
    }

    public static List<RideHistoryItemResponse> filterByStartDate(
            List<RideHistoryItemResponse> rides, LocalDate startDate) {
        List<RideHistoryItemResponse> filtered = new ArrayList<>();
        for (RideHistoryItemResponse ride : rides) {
            LocalDate rideDate = ride.getStartTime().toLocalDate();
            if (!rideDate.isBefore(startDate)) {
                filtered.add(ride);
            }
        }
        return filtered;
    }

    private List<RideHistoryItemResponse> filterByEndDate(
            List<RideHistoryItemResponse> rides, LocalDate endDate) {
        List<RideHistoryItemResponse> filtered = new ArrayList<>();
        for (RideHistoryItemResponse ride : rides) {
            LocalDate rideDate = ride.getStartTime().toLocalDate();
            if (!rideDate.isAfter(endDate)) {
                filtered.add(ride);
            }
        }
        return filtered;
    }

    public static List<RideHistoryItemResponse> generateMockRides() {
        return List.of(
                new RideHistoryItemResponse(
                        1L,
                        "44.7866,20.4489;44.8125,20.4612",
                        LocalDateTime.of(2025, 12, 20, 10, 30),
                        LocalDateTime.of(2025, 12, 20, 10, 55),
                        "Novi Sad, Bulevar oslobođenja 10",
                        "Novi Sad, Futoška 5",
                        false,
                        null,
                        1200.0,
                        false
                ),

                new RideHistoryItemResponse(
                        2L,
                        "44.8000,20.4500;44.8100,20.4600",
                        LocalDateTime.of(2025, 12, 21, 14, 15),
                        null,
                        "Novi Sad, Cara Lazara 15",
                        "Novi Sad, Jevrejska 22",
                        true,
                        CancelerType.DRIVER,
                        0.0,
                        false
                ),

                new RideHistoryItemResponse(
                        3L,
                        "44.7900,20.4550;44.8150,20.4700",
                        LocalDateTime.of(2025, 12, 22, 18, 45),
                        LocalDateTime.of(2025, 12, 22, 19, 20),
                        "Novi Sad, Šafarikova 8",
                        "Novi Sad, Zmaj Jovina 12",
                        false,
                        null,
                        1800.0,
                        true
                ),

                new RideHistoryItemResponse(
                        4L,
                        "44.7850,20.4400;44.8200,20.4800",
                        LocalDateTime.of(2025, 12, 23, 9, 0),
                        null,
                        "Novi Sad, Trg slobode 1",
                        "Novi Sad, Dunavska 30",
                        true,
                        CancelerType.PASSENGER,
                        0.0,
                        false
                ),

                new RideHistoryItemResponse(
                        5L,
                        "44.7950,20.4450;44.8050,20.4650",
                        LocalDateTime.of(2025, 12, 24, 16, 20),
                        LocalDateTime.of(2025, 12, 24, 16, 50),
                        "Novi Sad, Bulevar Cara Lazara 45",
                        "Novi Sad, Narodnog fronta 18",
                        false,
                        null,
                        950.0,
                        false
                )
        );
    }

    @GetMapping("/rides/{id}")
    public ResponseEntity<?> getRideDetails(@PathVariable("id") Long id) {

        List<RideHistoryItemResponse> allRides = generateMockRides();

        for (RideHistoryItemResponse ride : allRides) {
            if (ride.getId().equals(id)) {
                return ResponseEntity.ok(ride);  
            }
        }

        return ResponseEntity.status(404).body("Ride with ID " + id + " not found");
    }

    private List<RideHistoryItemResponse> sortRides(
            List<RideHistoryItemResponse> rides,
            RideSortBy sortBy,
            SortOrder sortOrder
    ) {
        if (sortBy == null) {
            sortBy = RideSortBy.START_TIME;
        }
        if (sortOrder == null) {
            sortOrder = SortOrder.DESC;
        }

        Comparator<RideHistoryItemResponse> comparator;

        switch (sortBy) {
            case PRICE ->
                    comparator = Comparator.comparing(RideHistoryItemResponse::getPrice);

            case END_TIME ->
                    comparator = Comparator.comparing(
                            RideHistoryItemResponse::getEndTime,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    );

            case CANCELLED ->
                    comparator = Comparator.comparing(RideHistoryItemResponse::isCancelled);

            case PANIC ->
                    comparator = Comparator.comparing(RideHistoryItemResponse::isPanicButtonPressed);

            default ->
                    comparator = Comparator.comparing(RideHistoryItemResponse::getStartTime);
        }

        if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
        }

        return rides.stream().sorted(comparator).toList();
    }

    @PostMapping("/registerDriver")
    public ResponseEntity<?> registerDriver(@Valid @RequestBody RegisterDriverRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        if (emailExists(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        RegisterDriverResponse response = getRegisterDriverResponse(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private static RegisterDriverResponse getRegisterDriverResponse(RegisterDriverRequest request) {
        Long newUserId = 200L;

        return new RegisterDriverResponse(
                newUserId,
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getAddress(),
                request.getPhoneNumber(),
                "default-avatar.png",
                UserRole.DRIVER,
                false,
                "Registration successful! Driver can check his email for activation link",
                request.getVehicleModel(),
                request.getVehicleType(),
                request.getPlateNum(),
                request.getSeatNum(),
                request.isBabySafe(),
                request.isPetSafe()
        );
    }


    private boolean emailExists(String email) {
        return "existing@example.com".equals(email);
    }

    @GetMapping("/driver-profile-requests")
    public ResponseEntity<List<DriverProfileChangeRequestResponse>> getRequests() {
        return ResponseEntity.ok(mockRequests);
    }

    private List<DriverProfileChangeRequestResponse> mockRequests = new ArrayList<>(List.of(
            new DriverProfileChangeRequestResponse(
                    1L,
                    41L,
                    new UpdateUserProfileRequest(),
                    DriverUpdateRequestStatus.PENDING
            ),
            new DriverProfileChangeRequestResponse(
                    2L,
                    42L,
                    new UpdateUserProfileRequest(),
                    DriverUpdateRequestStatus.PENDING
            )
    ));

    @PutMapping("/driver-profile-requests/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        mockRequests.stream()
                .filter(r -> r.getRequestId().equals(id))
                .findFirst()
                .ifPresent(r -> r.setStatus(DriverUpdateRequestStatus.APPROVED));

        return ResponseEntity.ok().build();
    }

    @PutMapping("/driver-profile-requests/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        mockRequests.stream()
                .filter(r -> r.getRequestId().equals(id))
                .findFirst()
                .ifPresent(r -> r.setStatus(DriverUpdateRequestStatus.REJECTED));

        return ResponseEntity.ok().build();
    }
}