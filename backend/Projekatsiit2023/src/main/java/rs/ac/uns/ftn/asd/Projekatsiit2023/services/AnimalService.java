package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.test.CreateAnimalRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Animal;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.AnimalRepository;

@Service
@Transactional
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository ar){
        this.animalRepository = ar;
    }

    public Animal Create(CreateAnimalRequest request){
        Animal animal = new Animal(request.getName(), request.getSpecies());
        return animalRepository.save(animal);
    }
}
