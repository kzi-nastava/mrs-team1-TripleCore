package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.request.LoginRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest request);
    String initiatePasswordReset(String email);
    String resetPassword(Long userId, String newPassword);
}
