package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.User;
import bd.edu.seu.lms.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    User saveUser(User user) {
        if (userRepo.existsById(user.getId()) || userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User already exist");
        }
        return userRepo.save(user);
    }

    User updateUser(User user) {
        if (user.getId() == null || !userRepo.existsById(user.getId())) {
            throw new IllegalArgumentException("User already exist");
        } else if (userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("This email already taken");
        }
        return userRepo.save(user);
    }

    void deleteUser(int id) {
        try {
            userRepo.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not delete User");
        }
    }

    List<User> getAllUser() {
        return userRepo.findAll();
    }

    boolean validateUser(String email, String password) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        if (user.getPassword().equals(password)) {
            return true;
        } else {
            throw new SecurityException(" Incorrect password.");
        }
    }
}
