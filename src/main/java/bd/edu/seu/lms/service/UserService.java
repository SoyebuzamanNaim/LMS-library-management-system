package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.User;
import bd.edu.seu.lms.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public User saveUser(User user) {
       
        if ((user.getId() != null && userRepo.existsById(user.getId())) || userRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepo.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        if (user.getId() == null || !userRepo.existsById(user.getId())) {
            throw new IllegalArgumentException("User does not exist");
        }

        User userExist = getUserByEmail(user.getEmail());
        if (userExist != null && !userExist.getId().equals(user.getId())) {
            throw new IllegalArgumentException("This email is already taken");
        }
        return userRepo.save(user);
    }

    @Transactional
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

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with this email"));
    }

}
