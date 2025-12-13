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
                new SubscriptionDto(null, "Standard", LocalDate.now(), LocalDate.now().plusYears(1), "Active"));
        return "subscription";
    }

    @PostMapping("/subscriptions/save")
    public String saveSubscription(@ModelAttribute("subscriptiondto") SubscriptionDto subscriptionDto,
            RedirectAttributes redirectAttributes) {
        if (subscriptionDto.studentId() == null || subscriptionDto.studentId() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Student is required");
            return "redirect:/subscriptions";
        }
        // Fetch Student entity
        var student = studentService.getStudentById(subscriptionDto.studentId());
        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/subscriptions";
        }

        Subscription subscription = new Subscription();
        subscription.setStudent(student);
        subscription.setType(subscriptionDto.type());
        subscription.setStartDate(subscriptionDto.startDate());
        subscription.setEndDate(subscriptionDto.endDate());
        subscription.setStatus(subscriptionDto.status());
        try {
            subscriptionService.saveSubscription(subscription);
            redirectAttributes.addFlashAttribute("success", "Subscription added successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/subscriptions";
    }

    @PostMapping("/subscriptions/update")
    public String updateSubscription(@ModelAttribute SubscriptionDto subscriptionDto, int id,
            RedirectAttributes redirectAttributes) {
        if (subscriptionDto.studentId() == null || subscriptionDto.studentId() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Student is required");
            return "redirect:/subscriptions";
        }
        // Fetch Student entity
        var student = studentService.getStudentById(subscriptionDto.studentId());
        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/subscriptions";
        }

        Subscription subscription = new Subscription();
        subscription.setStudent(student);
        subscription.setType(subscriptionDto.type());
        subscription.setStartDate(subscriptionDto.startDate());
        subscription.setEndDate(subscriptionDto.endDate());
        subscription.setStatus(subscriptionDto.status());
        try {
            subscriptionService.updateSubscription(id, subscription);
            redirectAttributes.addFlashAttribute("success", "Subscription updated successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/subscriptions";
    }

    @PostMapping("/subscriptions/delete")
    public String deleteSubscription(int id, RedirectAttributes redirectAttributes) {
        try {
            subscriptionService.deleteSubscription(id);
            redirectAttributes.addFlashAttribute("success", "Subscription deleted successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/subscriptions";
    }
}
