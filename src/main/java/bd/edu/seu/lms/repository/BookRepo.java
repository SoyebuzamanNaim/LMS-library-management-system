package bd.edu.seu.lms.repository;

import bd.edu.seu.lms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book,Integer> {
}
