package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.model.Allotment;
import bd.edu.seu.lms.repository.AllotmentRepo;
import bd.edu.seu.lms.repository.BookRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepo bookRepo;
    private final AllotmentRepo allotmentRepo;

    public BookService(BookRepo bookRepo, AllotmentRepo allotmentRepo) {
        this.bookRepo = bookRepo;
        this.allotmentRepo = allotmentRepo;
    }

    @Transactional
    public Book saveBook(Book book) {
        if (book.getId() != null && bookRepo.existsById(book.getId())) {
            throw new IllegalArgumentException("Book already exists");
        }
        if (book.getIsbn() != null && bookRepo.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        if (book.getAvailableCopies() > book.getTotalCopies() || book.getAvailableCopies() < 0
                || book.getTotalCopies() < 0) {
            throw new IllegalArgumentException(
                    "Available copies cannot exceed total copies and both must be non-negative");
        }
        return bookRepo.save(book);
    }

    @Transactional
    public Book updateBook(Book book) {
        if (book.getId() == null || !bookRepo.existsById(book.getId())) {
            throw new IllegalArgumentException("Book does not exist");
        }
        Book existingBook = bookRepo.findById(book.getId())
                .orElseThrow(() -> new IllegalArgumentException("Book does not exist"));
        if (book.getIsbn() != null && !book.getIsbn().equals(existingBook.getIsbn())) {
            Book bookWithIsbn = bookRepo.findByIsbn(book.getIsbn());
            if (bookWithIsbn != null && !bookWithIsbn.getId().equals(book.getId())) {
                throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
            }
        }
        if (book.getAvailableCopies() > book.getTotalCopies() || book.getAvailableCopies() < 0
                || book.getTotalCopies() < 0) {
            throw new IllegalArgumentException(
                    "Available copies cannot exceed total copies and both must be non-negative");
        }
        return bookRepo.save(book);
    }

    @Transactional
    public void deleteBook(int id) {
        if (!bookRepo.existsById(id)) {
            throw new IllegalArgumentException("Book does not exist");
        }

        List<Allotment> allotments = allotmentRepo.findByBook_Id(id);

        if (!allotments.isEmpty()) {
            StringBuilder middle = new StringBuilder();
            for (Allotment a : allotments) {
                middle.append(a.getId()).append(", ");
            }
            String s = "Cannot delete book. Allotments with IDs: " + middle.toString().replaceAll(", $", "")
                    + " are still active.";
            throw new IllegalArgumentException(s);
        }

        bookRepo.deleteById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public Book getBookById(int id) {
        return bookRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Book does not exist"));
    }

    // isfai
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        List<Book> byTitle = bookRepo.findByTitleContainingIgnoreCase(keyword);
        List<Book> byAuthor = bookRepo.findByAuthorContainingIgnoreCase(keyword);
        List<Book> combined = new ArrayList<>();
        combined.addAll(byTitle);
        combined.addAll(byAuthor);
        return combined.stream().distinct().collect(Collectors.toList());
    }

}
