package bd.edu.seu.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("email", session.getAttribute("email"));
        return "profile";
    }
}
