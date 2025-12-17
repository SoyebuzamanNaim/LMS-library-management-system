
package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Allotment;
import bd.edu.seu.lms.model.AllotmentStatus;
import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.model.BookStatus;
import bd.edu.seu.lms.repository.AllotmentRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllotmentService {
    private final AllotmentRepo allotmentRepo;
    private final BookService bookService;

    public AllotmentService(AllotmentRepo allotmentRepo, BookService bookService) {
        this.allotmentRepo = allotmentRepo;
        this.bookService = bookService;
    }

    @Transactional
    public Allotment saveAllotment(Allotment allotment) {
        if (allotment.getId() != null && allotmentRepo.existsById(allotment.getId())) {
            throw new IllegalArgumentException("Allotment already exists");
        }
        if (allotment.getBook() != null) {
            Book book = allotment.getBook();
            if (book.getAvailableCopies() == 0) {

                throw new IllegalArgumentException("Book is not available");
            }
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            if (book.getAvailableCopies() == 0) {
                book.setStatus(BookStatus.UNAVAILABLE);
            }
            bookService.updateBook(book);
        }
        return allotmentRepo.save(allotment);
    }

    @Transactional
    public Allotment updateAllotment(Allotment allotment) {
        if (allotment.getId() == null || !allotmentRepo.existsById(allotment.getId())) {
            throw new IllegalArgumentException("Allotment does not exist");
        }
        return allotmentRepo.save(allotment);
    }

    @Transactional
    public void deleteAllotment(int id) {
        Allotment allotment = allotmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Allotment does not exist"));

        if (allotment.getBook() != null) {
            Book book = allotment.getBook();
            if (allotment.getStatus() != AllotmentStatus.RETURNED) {
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookService.updateBook(book);
            }
        }

        try {
            allotmentRepo.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot delete allotment: " + e.getMessage());
        }
    }

    public List<Allotment> getAllAllotments() {
        return allotmentRepo.findAll();
    }

    public long countActiveAllotments() {
        return allotmentRepo.countByStatus(AllotmentStatus.ACTIVE);
    }

    public Allotment getAllotmentById(int id) {
        return allotmentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Allotment does not exist"));
    }

    public double calculateFine(LocalDate issueDate) {
        LocalDate plannedReturnDate = issueDate.plusDays(14);

        LocalDate today = LocalDate.now();

        if (!today.isAfter(plannedReturnDate)) {
            return 0;
        }

        // 20 takafor each day after the planned return date
        long overdueDays = ChronoUnit.DAYS.between(plannedReturnDate, today);
        // isfai
        return overdueDays * 20.0;
    }

    public List<Allotment> searchAllotments(String keyword) {
        if (keyword == null || keyword.trim().equals("")) {
            return getAllAllotments();
        }
        // isfai
        List<Allotment> studentMatches = allotmentRepo.findByStudent_NameContainingIgnoreCase(keyword);
        List<Allotment> titleMatches = allotmentRepo.findByBook_TitleContainingIgnoreCase(keyword);
        List<Allotment> combined = new ArrayList<>();

        combined.addAll(studentMatches);
        combined.addAll(titleMatches);

        return combined.stream().distinct().collect(Collectors.toList());
    }

}
