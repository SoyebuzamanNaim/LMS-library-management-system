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
        if (loginService.validateUser(loginDto.email(), loginDto.password())) {

            redirectAttributes.addFlashAttribute("success", session.getAttribute("user") + " logged in successfully");
            redirectAttributes.addFlashAttribute("email", session.getAttribute("email"));
            return "redirect:/dashboard";
        }
        redirectAttributes.addFlashAttribute("loginError", "Invalid email or password");
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Logged out successfully");
        return "redirect:/login";
    }
}
