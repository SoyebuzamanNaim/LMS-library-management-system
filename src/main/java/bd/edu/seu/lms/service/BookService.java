package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.repository.BookRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepo bookRepo;

    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Book saveBook(Book book) {
        // Validate foreign key relationships
        if (book.getPublication() == null) {
            throw new IllegalArgumentException("Publication is required");
        }
        if (book.getVendor() == null) {
            throw new IllegalArgumentException("Vendor is required");
        }
        // Derive status based on availability before saving
        if (book.getAvailableCopies() != null && book.getAvailableCopies() > 0) {
            book.setStatus("Available");
        } else {
            book.setStatus("Unavailable");
        }
        return bookRepo.save(book);
    }

    public Book updateBook(int id, Book book) {
        return bookRepo.findById(id).map(existing -> {
            existing.setTitle(book.getTitle());
            existing.setAuthor(book.getAuthor());
            existing.setPublication(book.getPublication());
            existing.setVendor(book.getVendor());
            existing.setCategory(book.getCategory());
            existing.setTotalCopies(book.getTotalCopies());
            existing.setAvailableCopies(book.getAvailableCopies());
            existing.setPricePerCopy(book.getPricePerCopy());
            if (existing.getAvailableCopies() != null && existing.getAvailableCopies() > 0) {
                existing.setStatus("Available");
            } else {
                existing.setStatus("Unavailable");
            }
            existing.setDescription(book.getDescription());
            return bookRepo.save(existing);
        }).orElse(null);
    }

    public void deleteBook(int id) {
        if (bookRepo.existsById(id)) {
            bookRepo.deleteById(id);
        }
    }

    public ArrayList<Book> getAllBooks() {
        return new ArrayList<>(bookRepo.findAll());
    }

    public Book getBookById(int id) {
        return bookRepo.findById(id).orElse(null);
    }

    public ArrayList<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(bookRepo.findAll());
        }
        String lowerKeyword = keyword.toLowerCase();
        return bookRepo.findAll().stream().filter(b -> {
            String title = b.getTitle() != null ? b.getTitle().toLowerCase() : "";
            String author = b.getAuthor() != null ? b.getAuthor().toLowerCase() : "";
            String category = b.getCategory() != null ? b.getCategory().toLowerCase() : "";
            return title.contains(lowerKeyword) || author.contains(lowerKeyword) || category.contains(lowerKeyword);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

}
