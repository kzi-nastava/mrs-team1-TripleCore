package rs.ac.uns.ftn.asd.Projekatsiit2023.services.impl;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Panic;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.PanicRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.services.PanicService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PanicServiceImpl implements PanicService {

    private final PanicRepository repository;

    public PanicServiceImpl(PanicRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Panic> getAllPanics() {
        return repository.findAllByOrderByTimeDesc();
    }

    @Override
    public List<Panic> getActivePanics() {
        return repository.findByResolvedFalseOrderByTimeDesc();
    }

    @Override
    public List<Panic> getResolvedPanics() {
        return repository.findByResolvedTrueOrderByTimeDesc();
    }

    @Override
    public void markAsResolved(Long id) {
        repository.findById(id).ifPresent(panic -> {
            panic.setResolved(true);
            repository.save(panic);
        });
    }

    @Override
    public Panic createPanic(Panic panic) {
        panic.setTime(LocalDateTime.now());
        panic.setResolved(false);
        return repository.save(panic);
    }
}
