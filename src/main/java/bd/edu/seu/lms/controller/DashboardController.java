package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.model.Allotment;
import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.model.Subscription;
import bd.edu.seu.lms.model.Vendor;
import bd.edu.seu.lms.service.AllotmentService;
import bd.edu.seu.lms.service.BookService;
import bd.edu.seu.lms.service.PublicationService;
import bd.edu.seu.lms.service.StudentService;
import bd.edu.seu.lms.service.SubscriptionService;
import bd.edu.seu.lms.service.VendorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final BookService bookService;
    private final StudentService studentService;
    private final AllotmentService allotmentService;
    private final SubscriptionService subscriptionService;
    private final VendorService vendorService;
    private final PublicationService publicationService;

    public DashboardController(BookService bookService, StudentService studentService,
            AllotmentService allotmentService, SubscriptionService subscriptionService,
            VendorService vendorService, PublicationService publicationService) {
        this.bookService = bookService;
        this.studentService = studentService;
        this.allotmentService = allotmentService;
        this.subscriptionService = subscriptionService;
        this.vendorService = vendorService;
        this.publicationService = publicationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        List<Book> books = bookService.getAllBooks();
        List<Student> students = studentService.getAllStudents();
        List<Allotment> allotments = allotmentService.getAllAllotments();
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        List<Vendor> vendors = vendorService.getAllVendors();

        long issuedBooks = allotments.stream()
                .filter(a -> a.getStatus() != null && a.getStatus().equalsIgnoreCase("Active"))
                .count();
        long activeSubscriptions = subscriptions.stream()
                .filter(s -> s.getStatus() != null && s.getStatus().equalsIgnoreCase("Active"))
                .count();

        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("totalBooks", books.size());
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("issuedBooks", issuedBooks);
        model.addAttribute("activeSubscriptions", activeSubscriptions);
        model.addAttribute("totalVendors", vendors.size());
        model.addAttribute("totalPublications", publicationService.getAllPublications().size());
        return "dashboard";
    }
}
