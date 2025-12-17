package bd.edu.seu.lms.repository;

import bd.edu.seu.lms.model.Allotment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllotmentRepo extends JpaRepository<Allotment, Integer> {
    List<Allotment> findByStudent_NameContainingIgnoreCase(String name);

    List<Allotment> findByBook_TitleContainingIgnoreCase(String title);

    List<Allotment> findByBook_Id(Integer bookId);

    List<Allotment> findByStudent_Id(Integer studentId);
}
