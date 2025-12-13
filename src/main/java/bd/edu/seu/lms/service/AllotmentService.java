
package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Allotment;
import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.repository.AllotmentRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class AllotmentService {
    private final AllotmentRepo allotmentRepo;
    private final BookService bookService;

    public AllotmentService(AllotmentRepo allotmentRepo, BookService bookService) {
        this.allotmentRepo = allotmentRepo;
        this.bookService = bookService;
    }

    public ArrayList<Allotment> getAllAllotments() {
        return new ArrayList<>(allotmentRepo.findAll());
    }

    public ArrayList<Allotment> searchAllotments(String keyword) {
        if (keyword == null || keyword.trim().equals("")) {
            return getAllAllotments();
        }
        String lowerKeyword = keyword.toLowerCase();
        return allotmentRepo.findAll().stream().filter(allotment -> {
            Student student = allotment.getStudent();
            Book book = allotment.getBook();
            String studentName = student != null && student.getName() != null ? student.getName().toLowerCase() : "";
            String bookTitle = book != null && book.getTitle() != null ? book.getTitle().toLowerCase() : "";
            String status = allotment.getStatus() != null ? allotment.getStatus().toLowerCase() : "";
            return studentName.contains(lowerKeyword) || bookTitle.contains(lowerKeyword)
                    || status.contains(lowerKeyword);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public Allotment getAllotmentById(int id) {
        if (id <= 0) {
            return null;
        }
        return allotmentRepo.findById(id).orElse(null);
    }

    public Allotment saveAllotment(Allotment allotment) {
        // Validate foreign key relationships
        if (allotment.getStudent() == null) {
            throw new IllegalArgumentException("Student is required");
        }
        if (allotment.getBook() == null) {
            throw new IllegalArgumentException("Book is required");
        }

        Book book = allotment.getBook();
        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new IllegalArgumentException("Book is not available");
        }

        // Automatically set return date to 14 days from issue date
        if (allotment.getIssueDate() != null) {
            allotment.setReturnDate(allotment.getIssueDate().plusDays(14));
        }
        if (allotment.getStatus() == null || allotment.getStatus().trim().equals("")) {
            allotment.setStatus("Active");
        }
        // Fine is 0 when creating, will be calculated when returning
        allotment.setFineAmount(0.0);
        Allotment saved = allotmentRepo.save(allotment);

        int copies = book.getAvailableCopies();
        book.setAvailableCopies(copies - 1);
        if (book.getAvailableCopies() <= 0) {
            book.setStatus("Unavailable");
        }
        bookService.updateBook(book.getId(), book);
        return saved;
    }

    public Allotment updateAllotment(int id, Allotment allotment) {
        // Validate foreign key relationships
        if (allotment.getStudent() == null) {
            throw new IllegalArgumentException("Student is required");
        }
        if (allotment.getBook() == null) {
            throw new IllegalArgumentException("Book is required");
        }

        return allotmentRepo.findById(id).map(existing -> {
            // Handle book change - return copy to old book, check new book availability
            if (existing.getBook().getId() != allotment.getBook().getId()) {
                // Return copy to old book
                Book oldBook = existing.getBook();
                int oldCopies = oldBook.getAvailableCopies() != null ? oldBook.getAvailableCopies() : 0;
                oldBook.setAvailableCopies(oldCopies + 1);
                if (oldBook.getAvailableCopies() > 0) {
                    oldBook.setStatus("Available");
                }
                bookService.updateBook(oldBook.getId(), oldBook);

                // Check new book availability
                Book newBook = allotment.getBook();
                if (newBook.getAvailableCopies() == null || newBook.getAvailableCopies() <= 0) {
                    throw new IllegalArgumentException("New book is not available");
                }
                // Decrement new book's available copies
                int newCopies = newBook.getAvailableCopies();
                newBook.setAvailableCopies(newCopies - 1);
                if (newBook.getAvailableCopies() <= 0) {
                    newBook.setStatus("Unavailable");
                }
                bookService.updateBook(newBook.getId(), newBook);
            }

            existing.setStudent(allotment.getStudent());
            existing.setBook(allotment.getBook());
            existing.setIssueDate(allotment.getIssueDate());
            // Automatically set return date to 14 days from issue date
            if (allotment.getIssueDate() != null) {
                existing.setReturnDate(allotment.getIssueDate().plusDays(14));
            }
            existing.setStatus(allotment.getStatus());
            // Fine is only calculated when returning the book, not during updates
            // Keep existing fine amount if status is already "Returned", otherwise set to 0
            if (!"Returned".equals(existing.getStatus())) {
                existing.setFineAmount(0.0);
            }
            return allotmentRepo.save(existing);
        }).orElse(null);
    }

    public void deleteAllotment(int id) {
        allotmentRepo.findById(id).ifPresent(existing -> {
            Book book = existing.getBook();
            if (book != null) {
                int copies = book.getAvailableCopies() != null ? book.getAvailableCopies() : 0;
                book.setAvailableCopies(copies + 1);
                if (book.getAvailableCopies() > 0) {
                    book.setStatus("Available");
                }
                bookService.updateBook(book.getId(), book);
            }
            allotmentRepo.deleteById(id);
        });
    }

    public void returnAllotment(int id) {
        allotmentRepo.findById(id).ifPresent(allotment -> {
            LocalDate actualReturnDate = LocalDate.now();
            LocalDate plannedReturnDate = allotment.getReturnDate();

            // Fine is only calculated if the actual return date has passed the planned
            // return date
            double fine = 0.0;
            if (plannedReturnDate != null && actualReturnDate.isAfter(plannedReturnDate)) {
                // Calculate fine: 20 taka/day for each day after the planned return date
                long overdueDays = ChronoUnit.DAYS.between(plannedReturnDate, actualReturnDate);
                fine = overdueDays * 20.0;
            }
            // If returned on or before the planned return date, fine remains 0

            allotment.setStatus("Returned");
            allotment.setFineAmount(fine);
            allotmentRepo.save(allotment);

            Book book = allotment.getBook();
            if (book != null) {
                Integer copies = book.getAvailableCopies() != null ? book.getAvailableCopies() : 0;
                book.setAvailableCopies(copies + 1);
                if (book.getAvailableCopies() > 0) {
                    book.setStatus("Available");
                }
                bookService.updateBook(book.getId(), book);
            }
        });
    }

    public double calculateFine(LocalDate issueDate, LocalDate plannedReturnDate) {
        if (issueDate == null || plannedReturnDate == null) {
            return 0.0;
        }

        LocalDate today = LocalDate.now();
        // Fine applies only if today is after the planned return date
        if (!today.isAfter(plannedReturnDate)) {
            return 0.0;
        }

        // Calculate fine: 20/day for each day after the planned return date
        long overdueDays = ChronoUnit.DAYS.between(plannedReturnDate, today);
        return overdueDays * 20.0;
    }

}
