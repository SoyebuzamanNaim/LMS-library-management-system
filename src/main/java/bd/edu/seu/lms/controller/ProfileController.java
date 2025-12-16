package bd.edu.seu.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import bd.edu.seu.lms.model.User;
import bd.edu.seu.lms.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    private UserService userService;
    public ProfileController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("email", session.getAttribute("email"));
        return "profile";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(HttpSession session, RedirectAttributes redirectAttributes) {
       
        User user = userService.getUserByEmail((String) session.getAttribute("email"));
        userService.deleteUser(user.getId());
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Account deleted successfully");
        return "redirect:/login";
    }
}
