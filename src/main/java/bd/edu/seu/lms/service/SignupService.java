package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SignupService {
    // memory
    private final ArrayList<User> users = new ArrayList<>();

    public void saveUser(User user) {
        if (users.stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            // IO.println("User email already exists");
            throw new IllegalArgumentException("User already exists");
        }
        users.add(user);
    }

    public ArrayList<User> getAllUsers() {
        // return a copy of the users list
        return new ArrayList<>(users);
    }
}
