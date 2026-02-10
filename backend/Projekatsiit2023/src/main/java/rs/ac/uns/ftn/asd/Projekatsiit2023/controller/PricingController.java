package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.common.VehiclePricesDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.price.ChangePricesRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.AdminService;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.PricingService;

@RestController
@RequestMapping("/api/prices")
public class PricingController {
    private final PricingService pricingService;
    private final AdminService adminService;

    public PricingController(
            PricingService pricingService,
            AdminService adminService
    ) {
        this.pricingService = pricingService;
        this.adminService = adminService;
    }

    @PostMapping("/init")
    public ResponseEntity<?> initPrices(){
        try{
            pricingService.initPrices();
            return ResponseEntity.ok("Prices initialized");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getPrices(){
        try{
            VehiclePricesDTO response = pricingService.getPrices();
            return ResponseEntity.ok(response);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/change")
    public ResponseEntity<?> changePrices(@Valid @RequestBody ChangePricesRequest request){
        try{
            if(adminService.isAdmin(request.adminId)){
                pricingService.setPrices(request.prices);
                return ResponseEntity.ok("Changed prices successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The user has no authority to change prices.");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
