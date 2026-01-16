package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.RegisterRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.RegisterResponse;

public interface RegisterService {
    RegisterResponse register(RegisterRequest request);
    String generateActivationLink(Long userId);
}
