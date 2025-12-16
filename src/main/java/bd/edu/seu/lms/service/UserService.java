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

    public User saveUser(User user) {
        if (userRepo.existsById(user.getId()) || userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepo.save(user);
    }

    public User updateUser(User user) {
        if (user.getId() == null || !userRepo.existsById(user.getId())) {
            throw new IllegalArgumentException("User does not exist");
        }
        // Check if email is taken by another user (not the current user)
        User existingUserWithEmail = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (existingUserWithEmail != null && !existingUserWithEmail.getId().equals(user.getId())) {
            throw new IllegalArgumentException("This email is already taken");
        }
        return userRepo.save(user);
    }

    public void deleteUser(int id) {
        try {
            userRepo.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot delete user");
        }
    }

    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    public User getUserById(int id) {
        return userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("No user found with this id"));
    }

}
