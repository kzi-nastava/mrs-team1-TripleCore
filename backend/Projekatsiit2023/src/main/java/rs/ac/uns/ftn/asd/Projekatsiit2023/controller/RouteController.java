package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.RouteService;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public  RouteController(RouteService routeService){
        this.routeService = routeService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> CreateTestRoute(){
        routeService.CreateTestRoute();
        return ResponseEntity.ok("Route created.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> GetRoute(@PathVariable("id") Long id){
        Route route;
        try{
            route = routeService.getRouteById(id);
        }
        catch (EntityNotFoundException nfe){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nfe.getMessage());
        }
        return ResponseEntity.ok(route);
    }
}
