package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RideEstimateRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RideEstimateResponse;

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

        switch (vehicleType.toUpperCase()) {
            case "STANDARD":
                return 300.0;
            case "LUXURY":
                return 800.0;
            case "VAN":
                return 600.0;
            default:
                return 300.0;
        }
    }

    private String generateRouteCoordinates() {
        return "44.7866,20.4489;44.8125,20.4612;44.8150,20.4630";
    }
}