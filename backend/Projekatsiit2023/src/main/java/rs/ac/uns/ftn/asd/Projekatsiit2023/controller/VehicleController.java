package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.ActiveRideVehicleDetailsResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.BasicVehicleDetailsResponse;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @GetMapping("/locations")
    public ResponseEntity<List<BasicVehicleDetailsResponse>> getVehicleLocations() {
        List<BasicVehicleDetailsResponse> vehicles = generateMockLocations();
        return ResponseEntity.ok(vehicles);
    }

    private List<BasicVehicleDetailsResponse> generateMockLocations() {
        return List.of(
                new BasicVehicleDetailsResponse(45.2671, 19.8335, true),
                new BasicVehicleDetailsResponse(45.2550, 19.8450, false)
        );
    }

    @GetMapping("/active-details")
    public ResponseEntity<ActiveRideVehicleDetailsResponse> getActiveRideVehicleDetails(){
        ActiveRideVehicleDetailsResponse details = getMockActiveVehicleDetails();
        return ResponseEntity.ok(details);
    }

    private ActiveRideVehicleDetailsResponse getMockActiveVehicleDetails(){
        return new ActiveRideVehicleDetailsResponse(45.2671, 19.8335, 369);
    }

}
