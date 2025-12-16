package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.repository.BookRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepo bookRepo;

    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Transactional
    public Book saveBook(Book book) {
        if (bookRepo.existsById(book.getId())) {
            throw new IllegalArgumentException("Book already exists");
        }
        return bookRepo.save(book);
    }

    @Transactional
    public Book updateBook(Book book) {
        if (book.getId() == null || !bookRepo.existsById(book.getId())) {
            throw new IllegalArgumentException("Book does not exist");
        }
        return bookRepo.save(book);
    }

    @Transactional
    public void deleteBook(int id) {
        try {
            bookRepo.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Book does not exist");
        }
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public Book getBookById(int id) {
        return bookRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Book does not exist"));
    }

    // isfai
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().equals("")) {
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
