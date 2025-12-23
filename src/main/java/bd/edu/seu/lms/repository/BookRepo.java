package bd.edu.seu.lms.repository;

import bd.edu.seu.lms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByVendor_Id(Integer vendorId);

    List<Book> findByPublication_Id(Integer publicationId);

    boolean existsByIsbn(String isbn);

    Book findByIsbn(String isbn);
}
