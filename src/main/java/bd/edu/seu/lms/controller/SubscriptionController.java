package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.SubscriptionDto;
import bd.edu.seu.lms.model.Subscription;
import bd.edu.seu.lms.service.StudentService;
import bd.edu.seu.lms.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

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
        if (search != null && !search.trim().equals("")) {
            model.addAttribute("search", search);
            model.addAttribute("subscriptions", subscriptionService.searchSubscriptions(search));
        } else {
            model.addAttribute("subscriptions", subscriptionService.getAllSubscriptions());
        }
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("subscriptiondto",
                new SubscriptionDto("", "Standard", LocalDate.now(), LocalDate.now().plusDays(30), "Active"));
        return "subscription";
    }

    @PostMapping("/subscriptions/save")
    public String saveSubscription(@ModelAttribute("subscriptiondto") SubscriptionDto subscriptionDto,
            RedirectAttributes redirectAttributes) {
        try {
            Subscription subscription = new Subscription();
            subscription.setStudentId(subscriptionDto.studentId());
            subscription.setType(subscriptionDto.type());
            subscription.setStartDate(subscriptionDto.startDate());
            subscription.setEndDate(subscriptionDto.endDate());
            subscription.setStatus(subscriptionDto.status());
            subscriptionService.saveSubscription(subscription);
            redirectAttributes.addFlashAttribute("success", "Subscription added successfully");
            return "redirect:/subscriptions";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/subscriptions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/subscriptions";
        }
    }

    @PostMapping("/subscriptions/update")
    public String updateSubscription(@ModelAttribute SubscriptionDto subscriptionDto, String id,
            RedirectAttributes redirectAttributes) {
        try {
            Subscription subscription = new Subscription();
            subscription.setStudentId(subscriptionDto.studentId());
            subscription.setType(subscriptionDto.type());
            subscription.setStartDate(subscriptionDto.startDate());
            subscription.setEndDate(subscriptionDto.endDate());
            subscription.setStatus(subscriptionDto.status());
            subscriptionService.updateSubscription(id, subscription);
            redirectAttributes.addFlashAttribute("success", "Subscription updated successfully");
            return "redirect:/subscriptions";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/subscriptions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/subscriptions";
        }
    }

    @PostMapping("/subscriptions/delete")
    public String deleteSubscription(String id, RedirectAttributes redirectAttributes) {
        try {
            subscriptionService.deleteSubscription(id);
            redirectAttributes.addFlashAttribute("success", "Subscription deleted successfully");
            return "redirect:/subscriptions";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/subscriptions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/subscriptions";
        }
    }
}
