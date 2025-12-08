package bd.edu.seu.lms.service;

import bd.edu.seu.lms.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final UserRepo userRepo;

    public LoginService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean validateUser(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        return userRepo.findByEmail(email)
                .map(user -> password.equals(user.getPassword()))
                .orElse(false);
    }

    public bd.edu.seu.lms.model.User findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        return userRepo.findByEmail(email).orElse(null);
    }
}
