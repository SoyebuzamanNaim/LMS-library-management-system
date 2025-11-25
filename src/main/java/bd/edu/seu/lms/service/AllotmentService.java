
package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Allotment;
import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.model.Student;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class AllotmentService {
    private final ArrayList<Allotment> allotments = new ArrayList<>();
    private long ind = 1;
    private final StudentService studentService;
    private final BookService bookService;

    public AllotmentService(StudentService studentService, BookService bookService) {
        this.studentService = studentService;
        this.bookService = bookService;
        loadDummyData();
    }

    public ArrayList<Allotment> getAllAllotments() {
        return new ArrayList<>(allotments);
    }

    public ArrayList<Allotment> searchAllotments(String keyword) {
        if (keyword == null || keyword.trim().equals("")) {
            return getAllAllotments();
        }
        String lowerKeyword = keyword.toLowerCase();
        return allotments.stream().filter(allotment -> {
            Student student = studentService.getStudentById(allotment.getStudentId());
            Book book = bookService.getBookById(allotment.getBookId());
            String studentName = student != null && student.getName() != null ? student.getName().toLowerCase() : "";
            String bookTitle = book != null && book.getTitle() != null ? book.getTitle().toLowerCase() : "";
            String status = allotment.getStatus() != null ? allotment.getStatus().toLowerCase() : "";
            return studentName.contains(lowerKeyword) || bookTitle.contains(lowerKeyword)
                    || status.contains(lowerKeyword);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public Allotment getAllotmentById(String id) {
        if (id == null) {
            return null;
        }
        return allotments.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public void saveAllotment(Allotment allotment) {
        simpleValidation(allotment);
        Book book = bookService.getBookById(allotment.getBookId());
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new IllegalArgumentException("Book is not available");
        }

        allotment.setId(Long.toString(ind++));
        if (allotment.getReturnDate() == null && allotment.getIssueDate() != null) {
            allotment.setReturnDate(allotment.getIssueDate().plusDays(14));
        }
        if (allotment.getStatus() == null || allotment.getStatus().trim().equals("")) {
            allotment.setStatus("Active");
        }
        allotments.add(allotment);

        int copies = book.getAvailableCopies();
        book.setAvailableCopies(copies - 1);
        if (book.getAvailableCopies() <= 0) {
            book.setStatus("Unavailable");
        }
    }

    public void updateAllotment(String id, Allotment allotment) {
        Allotment existing = getAllotmentById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Allotment not found");
        }
        simpleValidation(allotment);
        existing.setStudentId(allotment.getStudentId());
        existing.setBookId(allotment.getBookId());
        existing.setIssueDate(allotment.getIssueDate());
        existing.setReturnDate(allotment.getReturnDate());
        existing.setStatus(allotment.getStatus());
        existing.setFineAmount(allotment.getFineAmount());
    }

    public void deleteAllotment(String id) {
        Allotment existing = getAllotmentById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Allotment not found");
        }
        allotments.removeIf(a -> a.getId().equals(id));
        Book book = bookService.getBookById(existing.getBookId());
        if (book != null) {
            int copies = book.getAvailableCopies() != null ? book.getAvailableCopies() : 0;
            book.setAvailableCopies(copies + 1);
            if (book.getAvailableCopies() > 0) {
                book.setStatus("Available");
            }
        }
    }

    public void returnAllotment(String id) {
        Allotment allotment = getAllotmentById(id);
        if (allotment == null) {
            throw new IllegalArgumentException("Allotment not found");
        }
        allotment.setStatus("Returned");
        allotment.setReturnDate(LocalDate.now());
        Book book = bookService.getBookById(allotment.getBookId());
        if (book != null) {
            Integer copies = book.getAvailableCopies() != null ? book.getAvailableCopies() : 0;
            book.setAvailableCopies(copies + 1);
            if (book.getAvailableCopies() > 0) {
                book.setStatus("Available");
            }
        }
    }

    private void simpleValidation(Allotment allotment) {
        if (allotment == null) {
            throw new IllegalArgumentException("Allotment is required");
        }
        if (allotment.getStudentId() == null || allotment.getStudentId().trim().equals("")) {
            throw new IllegalArgumentException("Student is required");
        }
        if (studentService.getStudentById(allotment.getStudentId()) == null) {
            throw new IllegalArgumentException("Student not found");
        }
        if (allotment.getBookId() == null || allotment.getBookId().trim().equals("")) {
            throw new IllegalArgumentException("Book is required");
        }
        if (allotment.getIssueDate() == null) {
            throw new IllegalArgumentException("Issue date is required");
        }
    }

    private void loadDummyData() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Student> students = studentService.getAllStudents();
        ArrayList<Book> books = bookService.getAllBooks();
        if (students.isEmpty() || books.isEmpty()) {
            return;
        }

        for (int i = 0; i < 2; i++) {
            Student student = students.get(i);
            Book book = books.get(i);
            if (student == null || book == null) {
                continue;
            }

            Allotment allotment = new Allotment();
            allotment.setId(Long.toString(ind++));
            allotment.setStudentId(student.getId());
            allotment.setBookId(book.getId());
            allotment.setIssueDate(LocalDate.now().minusDays(i * 2L));
            allotment.setReturnDate(allotment.getIssueDate().plusDays(14));
            allotment.setStatus("Active");
            allotment.setFineAmount(0.0);
            allotments.add(allotment);
        }
    }

}
