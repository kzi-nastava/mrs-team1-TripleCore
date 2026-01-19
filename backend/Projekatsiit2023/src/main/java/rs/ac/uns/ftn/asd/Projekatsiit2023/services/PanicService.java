package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.models.Panic;

import java.util.List;

public interface PanicService {

    // return all panics descending by time
    List<Panic> getAllPanics();
    List<Panic> getActivePanics();
    List<Panic> getResolvedPanics();
    void markAsResolved(Long id);
    Panic createPanic(Panic panic);
}
