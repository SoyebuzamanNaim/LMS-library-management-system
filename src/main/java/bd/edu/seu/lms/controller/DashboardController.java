package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.service.AllotmentService;
import bd.edu.seu.lms.service.BookService;
import bd.edu.seu.lms.service.StudentService;
import bd.edu.seu.lms.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final BookService bookService;
    private final StudentService studentService;
    private final AllotmentService allotmentService;
    private final SubscriptionService subscriptionService;

    public DashboardController(BookService bookService, StudentService studentService,
            AllotmentService allotmentService, SubscriptionService subscriptionService) {
        this.bookService = bookService;
        this.studentService = studentService;
        this.allotmentService = allotmentService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("totalBooks", bookService.getAllBooks().size());
        model.addAttribute("totalStudents", studentService.getAllStudents().size());

        long issuedBooks = allotmentService.countActiveAllotments();
        model.addAttribute("issuedBooks", issuedBooks);

        long activeSubscriptions = subscriptionService.countActiveSubscriptions();
        model.addAttribute("activeSubscriptions", activeSubscriptions);

        return "dashboard";
    }
}
