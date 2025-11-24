package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.SignupDto;
import bd.edu.seu.lms.model.User;
import bd.edu.seu.lms.service.SignupService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignupController {
    // Constructor injection
    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @GetMapping("/signup")
    public String signup(Model model, HttpSession session) {
        if(session.getAttribute("user") != null){
            return  "redirect:/dashboard";
        }
        model.addAttribute("signupdto", new SignupDto("", "", ""));
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("signupdto") SignupDto signupdto,
            RedirectAttributes redirectAttributes , HttpSession session) {
        try {
            signupService.saveUser(new User(signupdto.username(), signupdto.email(), signupdto.password()));
            session.setAttribute("user", signupdto.username());
            session.setAttribute("email", signupdto.email());
            redirectAttributes.addFlashAttribute("success", "Account created! Please log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("signupError", e.getMessage());
            return "redirect:/signup";
        }
    }
}
