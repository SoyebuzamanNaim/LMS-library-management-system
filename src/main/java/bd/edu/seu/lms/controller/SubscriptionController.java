package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.SubscriptionDto;
import bd.edu.seu.lms.model.Subscription;
import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.model.SubscriptionType;
import bd.edu.seu.lms.model.SubscriptionStatus;
import bd.edu.seu.lms.service.StudentService;
import bd.edu.seu.lms.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
public class SubscriptionController {
    private SubscriptionService subscriptionService;
    private StudentService studentService;

    public SubscriptionController(SubscriptionService subscriptionService, StudentService studentService) {
        this.subscriptionService = subscriptionService;
        this.studentService = studentService;
    }

    @GetMapping("/subscriptions")
    public String subscriptions(HttpSession session, Model model, String search) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("search", search);
        model.addAttribute("subscriptions", subscriptionService.searchSubscriptions(search));

        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("subscriptiondto",
                new SubscriptionDto(null, null, null));
    return "subscription";
    }

    @PostMapping("/subscriptions/save")
    public String saveSubscription(@ModelAttribute("subscriptiondto") SubscriptionDto subscriptionDto,
            RedirectAttributes redirectAttributes) {
        try {
            Subscription subscription = new Subscription();
            subscription.setStudents(Arrays.asList(subscriptionDto.student()));
            subscription.setType(subscriptionDto.type());
            subscription.setStatus(subscriptionDto.status());
            subscriptionService.saveSubscription(subscription);

            redirectAttributes.addFlashAttribute("success", "Subscription added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/subscriptions";
    }

    @PostMapping("/subscriptions/update")
    public String updateSubscription(@ModelAttribute SubscriptionDto subscriptionDto, int id,
            RedirectAttributes redirectAttributes) {
        try {

            Subscription subscription = subscriptionService.getSubscriptionById(id);

            subscription.setType(subscriptionDto.type());
            subscription.setStatus(subscriptionDto.status());
            subscription.setStudents(Arrays.asList(subscriptionDto.student()));
            subscriptionService.updateSubscription(subscription);

            redirectAttributes.addFlashAttribute("success", "Subscription updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/subscriptions";
    }

    @PostMapping("/subscriptions/delete")
    public String deleteSubscription(int id, RedirectAttributes redirectAttributes) {
        try {
            subscriptionService.deleteSubscription(id);
            redirectAttributes.addFlashAttribute("success", "Subscription deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/subscriptions";
    }
}
