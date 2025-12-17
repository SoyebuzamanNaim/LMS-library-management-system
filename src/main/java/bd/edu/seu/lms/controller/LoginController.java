package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.LoginDto;
import bd.edu.seu.lms.model.User;
import bd.edu.seu.lms.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping({ "/", "/login" })
    public String login(Model model, HttpSession session) {

        model.addAttribute("logindto", new LoginDto("", ""));
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("logindto") LoginDto loginDto,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        try {
        if (loginService.validateUser(loginDto.email(), loginDto.password())) {
            User user = loginService.findByEmail(loginDto.email());
            session.setAttribute("user", user.getUsername());
            session.setAttribute("email", loginDto.email());
            redirectAttributes.addFlashAttribute("success", "Logged in successfully");
            return "redirect:/dashboard";
        } else {
                redirectAttributes.addFlashAttribute("error", "Invalid email or password");
                return "redirect:/login";
            }
        } catch (IllegalArgumentException | SecurityException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Logged out successfully");
        return "redirect:/login";
    }
}
