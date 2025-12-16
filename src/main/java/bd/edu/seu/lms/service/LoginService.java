package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.User;
import bd.edu.seu.lms.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class LoginService {
    private final UserRepo userRepo;

    public LoginService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Transactional
    public boolean validateUser(String email, String password) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User does not exist"));
        if (user.getPassword() == null || password == null) {
            throw new SecurityException("Incorrect password.");
            
        }
        if (user.getPassword().equals(password)) {
            return true;
        } else {
            throw new SecurityException("Incorrect password.");
        }
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User does not exist"));
    }
}
