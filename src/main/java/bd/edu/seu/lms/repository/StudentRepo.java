package bd.edu.seu.lms.repository;

import bd.edu.seu.lms.model.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {
    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByRollContainingIgnoreCase(String roll);

    List<Student> findByDepartmentContainingIgnoreCase(String department);

    List<Student> findByEmailContainingIgnoreCase(String email);
    
    boolean existsByRoll(String roll);
}
