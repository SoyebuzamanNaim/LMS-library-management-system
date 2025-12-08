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
        ArrayList<String> studentNames = buildStudentNames(allotments, students);
        ArrayList<String> bookTitles = buildBookTitles(allotments, books);
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("students", students);
        model.addAttribute("books", books);
        model.addAttribute("allotmentStudentNames", studentNames);
        model.addAttribute("allotmentBookTitles", bookTitles);
        model.addAttribute("allotmentdto",
                new AllotmentDto("", "", LocalDate.now(), LocalDate.now().plusDays(14), "Active", 0.0));

        return "allotments";
    }

    @PostMapping("/allotment/save")
    public String saveAllotment(@ModelAttribute("allotmentdto") AllotmentDto allotmentDto,
            RedirectAttributes redirectAttributes) {
        try {
            Allotment allotment = new Allotment();
            allotment.setStudentId(allotmentDto.studentId());
            allotment.setBookId(allotmentDto.bookId());
            allotment.setIssueDate(allotmentDto.issueDate());
            allotment.setReturnDate(allotmentDto.returnDate());
            allotment.setStatus(allotmentDto.status());
            allotment
                    .setFineAmount(allotmentService.calculateFine(allotment.getIssueDate(), allotment.getReturnDate()));
            allotmentService.saveAllotment(allotment);
            redirectAttributes.addFlashAttribute("success", "Allotment created successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create allotment: " + e.getMessage());
        }
        return "redirect:/allotment";
    }

    @PostMapping("/allotment/update")
    public String updateAllotment(@ModelAttribute AllotmentDto allotmentDto, String id,
            RedirectAttributes redirectAttributes) {
        try {
            Allotment allotment = new Allotment();
            allotment.setStudentId(allotmentDto.studentId());
            allotment.setBookId(allotmentDto.bookId());
            allotment.setIssueDate(allotmentDto.issueDate());
            allotment.setReturnDate(allotmentDto.returnDate());
            allotment.setStatus(allotmentDto.status());
            allotment
                    .setFineAmount(allotmentService.calculateFine(allotmentDto.issueDate(), allotmentDto.returnDate()));
            allotmentService.updateAllotment(id, allotment);
            redirectAttributes.addFlashAttribute("success", "Allotment updated successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update allotment: " + e.getMessage());
        }
        return "redirect:/allotment";
    }

    @PostMapping("/allotment/delete")
    public String deleteAllotment(String id, RedirectAttributes redirectAttributes) {
        try {
            allotmentService.deleteAllotment(id);
            redirectAttributes.addFlashAttribute("success", "Allotment deleted successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete allotment: " + e.getMessage());
        }
        return "redirect:/allotment";
    }

    @PostMapping("/allotment/return")
    public String returnAllotment(String id, RedirectAttributes redirectAttributes) {
        try {
            allotmentService.returnAllotment(id);
            redirectAttributes.addFlashAttribute("success", "BookRepo returned successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to return book: " + e.getMessage());
        }
        return "redirect:/allotment";
    }

    private ArrayList<String> buildStudentNames(List<Allotment> allotments, List<Student> students) {
        ArrayList<String> names = new ArrayList<>();
        for (Allotment allotment : allotments) {
            names.add(findStudentName(students, allotment.getStudentId()));
        }
        return names;
    }

    private ArrayList<String> buildBookTitles(List<Allotment> allotments, List<Book> books) {
        ArrayList<String> titles = new ArrayList<>();
        for (Allotment allotment : allotments) {
            titles.add(findBookTitle(books, allotment.getBookId()));
        }
        return titles;
    }

    private String findStudentName(List<Student> students, String studentId) {
        if (studentId == null) {
            return "Unknown Student";
        }
        for (Student student : students) {
            if (studentId.equals(student.getId()) && student.getName() != null) {
                return student.getName();
            }
        }
        return "Unknown Student";
    }

    private String findBookTitle(List<Book> books, String bookId) {
        if (bookId == null) {
            return "Unknown BookRepo";
        }
        for (Book book : books) {
            if (bookId.equals(book.getId()) && book.getTitle() != null) {
                return book.getTitle();
            }
        }
        return "Unknown BookRepo";
    }
}
