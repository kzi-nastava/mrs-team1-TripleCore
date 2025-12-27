package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.VehicleLocationResponse;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @GetMapping("/locations")
    public ResponseEntity<List<VehicleLocationResponse>> getVehicleLocations() {
        List<VehicleLocationResponse> vehicles = generateMockVehicles();
        return ResponseEntity.ok(vehicles);
    }

    private List<VehicleLocationResponse> generateMockVehicles() {
        return List.of(
                new VehicleLocationResponse(45.2671, 19.8335, true),
                new VehicleLocationResponse(45.2550, 19.8450, false)
        );
    }

}
