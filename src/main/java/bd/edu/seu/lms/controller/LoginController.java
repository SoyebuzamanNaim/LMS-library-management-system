package bd.edu.seu.lms.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    @GetMapping({"/","/login"})
    public String login(Model model , HttpSession session , RedirectAttributes redirectAttributes ) {
        if( session.getAttribute("user") == null ) {return  "login";}
         return "dashboard";
    }
     @PostMapping("/login")
     public String login(Model model){
     return "login";
     }
}
