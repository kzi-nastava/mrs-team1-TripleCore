package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

public interface AccountActivationService {
    boolean activateUser(Long userId);

    boolean canActivate(Long userId);
    boolean activateUserWithPassword(Long userId, String password);

}