package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.AdminRepository;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository ar){
        this.adminRepository = ar;
    }

    public boolean isAdmin(Long id){
        return adminRepository.findById(id).isPresent();
    }
}
