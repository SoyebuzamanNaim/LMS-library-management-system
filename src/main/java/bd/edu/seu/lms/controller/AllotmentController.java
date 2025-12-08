package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.AllotmentDto;
import bd.edu.seu.lms.model.Allotment;
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
import java.util.ArrayList;
import java.util.List;

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

        List<Allotment> allotments;
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("search", search);
            allotments = allotmentService.searchAllotments(search);
        } else {
            allotments = allotmentService.getAllAllotments();
        }
        model.addAttribute("allotments", allotments);

        List<Student> students = studentService.getAllStudents();
        List<Book> books = bookService.getAllBooks();
        ArrayList<String> studentNames = buildStudentNames(allotments);
        ArrayList<String> bookTitles = buildBookTitles(allotments);
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("students", students);
        model.addAttribute("books", books);
        model.addAttribute("allotmentStudentNames", studentNames);
        model.addAttribute("allotmentBookTitles", bookTitles);
        model.addAttribute("allotmentdto",
                new AllotmentDto(null, null, LocalDate.now(), LocalDate.now().plusDays(14), "Active", 0.0));

        return "allotments";
    }

    @PostMapping("/allotment/save")
    public String saveAllotment(@ModelAttribute("allotmentdto") AllotmentDto allotmentDto,
            RedirectAttributes redirectAttributes) {
        if (allotmentDto.studentId() == null || allotmentDto.studentId() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Student is required");
            return "redirect:/allotment";
        }
        if (allotmentDto.bookId() == null || allotmentDto.bookId() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Book is required");
            return "redirect:/allotment";
        }
        // Fetch Student and Book entities
        var student = studentService.getStudentById(allotmentDto.studentId());
        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/allotment";
        }
        var book = bookService.getBookById(allotmentDto.bookId());
        if (book == null) {
            redirectAttributes.addFlashAttribute("error", "Book not found");
            return "redirect:/allotment";
        }

        Allotment allotment = new Allotment();
        allotment.setStudent(student);
        allotment.setBook(book);
        // Issue date defaults to today if not provided
        allotment.setIssueDate(allotmentDto.issueDate() != null ? allotmentDto.issueDate() : LocalDate.now());
        // Return date will be automatically set to 14 days from issue date in service
        allotment.setStatus(allotmentDto.status() != null ? allotmentDto.status() : "Active");
        // Fine will be calculated when returning the book
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
        if (allotmentDto.studentId() == null || allotmentDto.studentId() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Student is required");
            return "redirect:/allotment";
        }
        if (allotmentDto.bookId() == null || allotmentDto.bookId() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Book is required");
            return "redirect:/allotment";
        }
        // Fetch Student and Book entities
        var student = studentService.getStudentById(allotmentDto.studentId());
        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/allotment";
        }
        var book = bookService.getBookById(allotmentDto.bookId());
        if (book == null) {
            redirectAttributes.addFlashAttribute("error", "Book not found");
            return "redirect:/allotment";
        }

        Allotment allotment = new Allotment();
        allotment.setStudent(student);
        allotment.setBook(book);
        // Issue date defaults to today if not provided
        allotment.setIssueDate(allotmentDto.issueDate() != null ? allotmentDto.issueDate() : LocalDate.now());
        // Return date will be automatically set to 14 days from issue date in service
        allotment.setStatus(allotmentDto.status() != null ? allotmentDto.status() : "Active");
        // Fine will be recalculated in service based on current date vs return date
        try {
            if (allotmentService.updateAllotment(id, allotment) == null) {
                redirectAttributes.addFlashAttribute("error", "Allotment not found");
            } else {
                redirectAttributes.addFlashAttribute("success", "Allotment updated successfully");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/allotment";
    }

    @PostMapping("/allotment/delete")
    public String deleteAllotment(int id, RedirectAttributes redirectAttributes) {
        allotmentService.deleteAllotment(id);
        redirectAttributes.addFlashAttribute("success", "Allotment deleted successfully");
        return "redirect:/allotment";
    }

    @PostMapping("/allotment/return")
    public String returnAllotment(int id, RedirectAttributes redirectAttributes) {
        allotmentService.returnAllotment(id);
        redirectAttributes.addFlashAttribute("success", "Book returned successfully");
        return "redirect:/allotment";
    }

    private ArrayList<String> buildStudentNames(List<Allotment> allotments) {
        ArrayList<String> names = new ArrayList<>();
        for (Allotment allotment : allotments) {
            Student student = allotment.getStudent();
            if (student != null && student.getName() != null) {
                names.add(student.getName());
            } else {
                names.add("Unknown Student");
            }
        }
        return names;
    }

    private ArrayList<String> buildBookTitles(List<Allotment> allotments) {
        ArrayList<String> titles = new ArrayList<>();
        for (Allotment allotment : allotments) {
            Book book = allotment.getBook();
            if (book != null && book.getTitle() != null) {
                titles.add(book.getTitle());
            } else {
                titles.add("Unknown Book");
            }
        }
        return titles;
    }
}
