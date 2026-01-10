package rs.ac.uns.ftn.asd.Projekatsiit2023.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.CreateAnimalRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.AnimalService;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService as){
        this.animalService = as;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAnimal(@RequestBody CreateAnimalRequest request){
        animalService.Create(request);
        return ResponseEntity.ok("Animal created");
    }
}
