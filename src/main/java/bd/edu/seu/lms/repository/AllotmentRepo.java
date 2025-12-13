package bd.edu.seu.lms.repository;

import bd.edu.seu.lms.model.Allotment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllotmentRepo extends JpaRepository<Allotment, Integer> {
}
