package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.User;
import bd.edu.seu.lms.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SignupService {
    private final UserRepo userRepo;

    public SignupService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void saveUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }
        userRepo.save(user);
    }

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(userRepo.findAll());
    }
}
