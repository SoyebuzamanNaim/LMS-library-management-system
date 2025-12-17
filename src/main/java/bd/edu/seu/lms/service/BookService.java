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
        if(book.getAvailableCopies()>book.getTotalCopies()||book.getAvailableCopies()<0||book.getTotalCopies()<0){
            throw new IllegalArgumentException("Check Available book and Total books ");
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
        if (!bookRepo.existsById(id)) {
            throw new IllegalArgumentException("Book does not exist");
        }

        
        List<Allotment> allotments = allotmentRepo.findByBook_Id(id);

        if (!allotments.isEmpty()) {
            String middle="";
            for(Allotment a:allotments){middle+=(a.getId()+", ");}
            String s = "Cannot delete book " +
                    middle
                    + " allotments of this book still active.";
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
