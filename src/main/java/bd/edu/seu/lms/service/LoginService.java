package bd.edu.seu.lms.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final SignupService signupService;

    public LoginService(SignupService signupService) {
        this.signupService = signupService;
    }

    public boolean validateUser(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        return signupService.getAllUsers().stream()
                .anyMatch(user -> user.getEmail().equals(email) && user.getPassword().equals(password));
    }
}
