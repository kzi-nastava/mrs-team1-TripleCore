package rs.ac.uns.ftn.asd.Projekatsiit2023.services;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.asd.Projekatsiit2023.dto.response.UserBlockedResponse;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.User;
import rs.ac.uns.ftn.asd.Projekatsiit2023.models.UserBlock;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.AdminRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserBlockRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2023.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    private final UserBlockRepository userBlockRepository;

    public AdminService(AdminRepository ar, UserRepository userRepository, UserBlockRepository userBlockRepository){
        this.adminRepository = ar;
        this.userRepository = userRepository;
        this.userBlockRepository = userBlockRepository;
    }

    public boolean isAdmin(Long id){
        return adminRepository.findById(id).isPresent();
    }

    public List<UserBlockedResponse> getAllNonAdminUsers(){

        List<User> users = userRepository.findByRoleNot(UserRole.ADMIN);

        return users.stream()
                .map(UserBlockedResponse::new)
                .toList();
    }

    public UserBlockedResponse blockUser(Long userId, String note) throws Exception {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = userOpt.get();

        if (user.isAccountBlocked()) {
            throw new Exception("User is already blocked");
        }


        user.setAccountBlocked(true);
        userRepository.save(user);


        UserBlock block = new UserBlock();
        block.setUserId(userId);
        block.setNote(note);
        userBlockRepository.save(block);

        return new UserBlockedResponse(user);
    }

    public String getBlockedNoteForUser(Long userId) {

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return "";
        }

        User user = userOpt.get();


        if (!user.isAccountBlocked()) {
            return "";
        }


        Optional<UserBlock> blockOpt = userBlockRepository.findByUserId(userId);


        return blockOpt
                .map(UserBlock::getNote)
                .filter(note -> note != null && !note.trim().isEmpty())
                .orElse("");
    }


}
