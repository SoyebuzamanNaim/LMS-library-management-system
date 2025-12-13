package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.LoginDto;
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

        if (!model.containsAttribute("logindto")) {
            model.addAttribute("logindto", new LoginDto("", ""));
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("logindto") LoginDto loginDto,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        if (!loginService.validateUser(loginDto.email(), loginDto.password())) {
        redirectAttributes.addFlashAttribute("loginError", "Invalid email or password");
        return "redirect:/login";
        }

        var user = loginService.findByEmail(loginDto.email());
        session.setAttribute("user", user != null ? user.getUsername() : loginDto.email());
        session.setAttribute("email", loginDto.email());
        redirectAttributes.addFlashAttribute("success", "Logged in successfully");
        return "redirect:/dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Logged out successfully");
        return "redirect:/login";
    }
}
