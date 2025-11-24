package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final ArrayList<Book> books = new ArrayList<>();
    private long ind = 1;

    public void saveBook(Book book) {
        if (books.stream().anyMatch(b -> b.getId().equals(book.getId()))) {
            throw new IllegalArgumentException("Book already exists");
        }
        if (books.stream().anyMatch(b -> b.getTitle().equals(book.getTitle()))) {
            throw new IllegalArgumentException("Book with this title already exists");
        }
        book.setId(Long.toString(ind++));
        books.add(book);
    }

    public void updateBook(String id, Book book) {
        if (!books.stream().anyMatch(b -> b.getId().equals(id))) {
            throw new IllegalArgumentException("Book not found");
        }
        if (books.stream().anyMatch(b -> b.getTitle().equals(book.getTitle()))) {
            throw new IllegalArgumentException("Book with this title already exists");
        }
        books.stream().filter(b -> b.getId().equals(id)).findFirst().ifPresent(b -> {
            b.setTitle(book.getTitle());
            b.setAuthor(book.getAuthor());
            b.setPublicationId(book.getPublicationId());
            b.setVendorId(book.getVendorId());
            b.setCategory(book.getCategory());
            b.setTotalCopies(book.getTotalCopies());
            b.setAvailableCopies(book.getAvailableCopies());
            b.setPricePerCopy(book.getPricePerCopy());
            // Set status based on available copies
            b.setStatus(b.getAvailableCopies() != null && b.getAvailableCopies() > 0 ? "Available" : "Unavailable");
            b.setDescription(book.getDescription());
        });
    }

    public void deleteBook(String id) {
        if (!books.stream().anyMatch(b -> b.getId().equals(id))) {
            throw new IllegalArgumentException("Book not found");
        }
        books.removeIf(b -> b.getId().equals(id));
    }

    public ArrayList<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public Book getBookById(String id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
    }

    public ArrayList<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(books);
        }
        String lowerKeyword = keyword.toLowerCase();
        return books.stream().filter(b -> {
            String title = b.getTitle() != null ? b.getTitle().toLowerCase() : "";
            String author = b.getAuthor() != null ? b.getAuthor().toLowerCase() : "";
            String category = b.getCategory() != null ? b.getCategory().toLowerCase() : "";
            return title.contains(lowerKeyword) || author.contains(lowerKeyword) || category.contains(lowerKeyword);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    // Initialize with dummy data
    public BookService() {
        initializeDummyData();
    }

    private void initializeDummyData() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Book b1 = new Book();
        b1.setId(Long.toString(ind++));
        b1.setTitle("Rich Dad Poor Dad");
        b1.setAuthor("Robert Kiyosaki");
        b1.setCategory("Personal Development");
        b1.setTotalCopies(10);
        b1.setAvailableCopies(8);
        b1.setPricePerCopy(25.99);
        b1.setStatus(b1.getAvailableCopies() > 0 ? "Available" : "Unavailable");
        b1.setDescription("A classic book on personal development");
        b1.setPublicationId("1");
        b1.setVendorId("1");
        books.add(b1);

        Book b2 = new Book();
        b2.setId(Long.toString(ind++));
        b2.setTitle("Atomic Habits");
        b2.setAuthor("James Clear");
        b2.setCategory("Personal Development");
        b2.setTotalCopies(5);
        b2.setAvailableCopies(0);
        b2.setPricePerCopy(89.99);
        b2.setStatus(b2.getAvailableCopies() > 0 ? "Available" : "Unavailable");
        b2.setDescription("A comprehensive guide to building good habits and breaking bad ones");
        b2.setPublicationId("2");
        b2.setVendorId("2");
        books.add(b2);

        Book b3 = new Book();
        b3.setId(Long.toString(ind++));
        b3.setTitle("SOLID Principles");
        b3.setAuthor("Uncle Bob");
        b3.setCategory("Programming");
        b3.setTotalCopies(15);
        b3.setAvailableCopies(12);
        b3.setPricePerCopy(45.00);
        b3.setStatus(b3.getAvailableCopies() > 0 ? "Available" : "Unavailable");
        b3.setDescription("A comprehensive guide to the SOLID principles of object-oriented design");
        b3.setPublicationId("1");
        b3.setVendorId("1");
        books.add(b3);
    }
}
