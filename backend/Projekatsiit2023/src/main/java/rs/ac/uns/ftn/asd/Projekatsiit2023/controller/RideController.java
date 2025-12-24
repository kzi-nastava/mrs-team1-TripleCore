package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideCancelRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideEstimateRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideCancelResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideEstimateResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.CancelerType;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    @PostMapping("/estimate")
    public ResponseEntity<RideEstimateResponse> estimateRide(
            @Valid @RequestBody RideEstimateRequest request) {

        Double distance = calculateDistance(request.getStartAddress(), request.getEndAddress());

        Integer time = calculateEstimatedTime(distance);

        Double price = calculatePriceBySpecification(distance, request.getVehicleType().name());

        String route = generateRouteCoordinates();

        RideEstimateResponse response = new RideEstimateResponse(
                price,
                time,
                distance,
                route,
                String.format("Ride estimate: %.0f RSD for %.1f km, estimated time: %d min",
                        price, distance, time)
        );

        return ResponseEntity.ok(response);
    }

    private Double calculateDistance(String start, String end) {
        return 7.5;
    }

    private Integer calculateEstimatedTime(Double distance) {
        return 5 + (int)(distance * 2);
    }

    /**
     * cena_po_tipu_vozila + broj_kilometara * 120
     */
    private Double calculatePriceBySpecification(Double distance, String vehicleType) {
        double basePricePerVehicleType = getBasePriceForVehicleType(vehicleType);

        return basePricePerVehicleType + (distance * 120.0);
    }

    private double getBasePriceForVehicleType(String vehicleType) {
        if (vehicleType == null) {
            return 300.0; // default STANDARD
        }

        return switch (vehicleType.toUpperCase()) {
            case "STANDARD" -> 300.0;
            case "LUXURY" -> 800.0;
            case "VAN" -> 600.0;
            default -> 300.0;
        };
    }

    private String generateRouteCoordinates() {
        return "44.7866,20.4489;44.8125,20.4612;44.8150,20.4630";
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRide(
            @PathVariable("id") Long id,
            @Valid @RequestBody RideCancelRequest request) {

        if (!rideExists(id)) {
            return ResponseEntity.status(404)
                    .body("Ride with ID " + id + " not found");
        }

        CancelerType cancelerType = request.getCancelerType();
        String reason = request.getReason();

        if (cancelerType == CancelerType.DRIVER && (reason == null || reason.trim().isEmpty())) {
            return ResponseEntity.badRequest()
                    .body("Driver must provide a cancellation reason");
        }

        if (cancelerType == CancelerType.PASSENGER) {
            if (!canPassengerCancel(id)) {
                return ResponseEntity.badRequest()
                        .body("Passenger can only cancel 10 minutes before ride start");
            }
        }

        boolean success = true;

        RideCancelResponse response = new RideCancelResponse(success, cancelerType, reason);

        return ResponseEntity.ok(response);
    }

    private boolean canPassengerCancel(Long id) {
        return id >= 3;
    }

    private boolean rideExists(Long id) {
        return id >= 1 && id <= 5;
    }
}