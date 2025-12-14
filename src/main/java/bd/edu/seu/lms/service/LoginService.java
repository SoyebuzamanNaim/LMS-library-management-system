package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.User;
import bd.edu.seu.lms.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final UserRepo userRepo;

    public LoginService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean validateUser(String email, String password) {
        if (email == null || email.trim().equals("") || password == null || password.trim().equals("")) {
            return false;
        }
        Optional<User> user = userRepo.findByEmail(email);
        boolean isValid= user.isPresent() && user.get().getPassword().equals(password);
        if(!isValid) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return true;
    }
}
