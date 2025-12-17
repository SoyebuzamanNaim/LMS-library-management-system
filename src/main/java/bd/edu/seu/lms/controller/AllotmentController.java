package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.AllotmentDto;
import bd.edu.seu.lms.model.Allotment;
import bd.edu.seu.lms.model.AllotmentStatus;
import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.service.AllotmentService;
import bd.edu.seu.lms.service.BookService;
import bd.edu.seu.lms.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class AllotmentController {

    private final AllotmentService allotmentService;
    private final StudentService studentService;
    private final BookService bookService;

    public AllotmentController(AllotmentService allotmentService, StudentService studentService,
            BookService bookService) {
        this.allotmentService = allotmentService;
        this.studentService = studentService;
        this.bookService = bookService;
    }

    @GetMapping("/allotment")
    public String allotments(HttpSession session, Model model, String search) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("search", search);
        model.addAttribute("allotments", allotmentService.searchAllotments(search));

        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("allotmentdto",
                new AllotmentDto(null, null, LocalDate.now(), AllotmentStatus.ACTIVE, 0.0));
        return "allotments";
    }

    @PostMapping("/allotment/save")
    public String saveAllotment(@ModelAttribute("allotmentdto") AllotmentDto allotmentDto,
            RedirectAttributes redirectAttributes) {

        Student student = studentService.getStudentById(allotmentDto.studentId());
        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/allotment";
        }
        Book book = bookService.getBookById(allotmentDto.bookId());
        if (book == null) {
            redirectAttributes.addFlashAttribute("error", "Book not found");
            return "redirect:/allotment";
        }

        Allotment allotment = new Allotment();
        allotment.setStudent(student);
        allotment.setBook(book);
        allotment.setIssueDate(allotmentDto.issueDate());
        allotment.setStatus(allotmentDto.status());
        allotment.setFineAmount(allotmentService.calculateFine(allotmentDto.issueDate()));

        try {
            allotmentService.saveAllotment(allotment);
            redirectAttributes.addFlashAttribute("success", "Allotment created successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/allotment";
    }

    @PostMapping("/allotment/update")
    public String updateAllotment(@ModelAttribute AllotmentDto allotmentDto, int id,
            RedirectAttributes redirectAttributes) {

        Student student = studentService.getStudentById(allotmentDto.studentId());
        if (student == null || allotmentDto.studentId() == null) {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/allotment";
        }
        Book book = bookService.getBookById(allotmentDto.bookId());
        if (book == null || allotmentDto.bookId() == null) {
            redirectAttributes.addFlashAttribute("error", "Book not found");
            return "redirect:/allotment";
        }

        Allotment allotment = allotmentService.getAllotmentById(id);
        allotment.setStudent(student);
        allotment.setBook(book);
        allotment.setIssueDate(allotmentDto.issueDate());
        allotment.setStatus(allotmentDto.status());

        allotment.setFineAmount(allotmentService.calculateFine(allotmentDto.issueDate()));
        try {
            allotmentService.updateAllotment(allotment);
            redirectAttributes.addFlashAttribute("success", "Allotment updated successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/allotment";
    }

    @PostMapping("/allotment/delete")
    public String deleteAllotment(int id, RedirectAttributes redirectAttributes) {
        try {
            allotmentService.deleteAllotment(id);
            redirectAttributes.addFlashAttribute("success", "Allotment deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/allotment";
    }

    @PostMapping("/allotment/return")
    public String returnAllotment(int id, RedirectAttributes redirectAttributes) {
        try {
            Allotment allotment = allotmentService.getAllotmentById(id);

            allotment.setStatus(AllotmentStatus.RETURNED);

            allotment.setFineAmount(allotmentService.calculateFine(allotment.getIssueDate()));
            allotmentService.updateAllotment(allotment);

            redirectAttributes.addFlashAttribute("success", "Book returned successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/allotment";
    }

}
