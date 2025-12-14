package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.repository.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepo studentRepo;

    public StudentService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Student saveStudent(Student student) {
        if (student.getId() != null) {
            throw new IllegalArgumentException("Student already exists");
        }
        return studentRepo.save(student);
    }

    public Student updateStudent(Student student) {
        if (student.getId() == null) {
            throw new IllegalArgumentException("Student does not exist");
        }
        return studentRepo.save(student);
    }

    public void deleteStudent(int id) {
        try {
            studentRepo.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Student does not exist");
        }
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Student getStudentById(int id) {
        return studentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Student does not exist"));
    }

    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return studentRepo.findAll();
        }
        // Idea stolen from ChatGPT to remove duplicates
        List<Student> nameMatches = studentRepo.findByNameContainingIgnoreCase(keyword);
        List<Student> rollMatches = studentRepo.findByRollContainingIgnoreCase(keyword);
        List<Student> departmentMatches = studentRepo.findByDepartmentContainingIgnoreCase(keyword);
        List<Student> emailMatches = studentRepo.findByEmailContainingIgnoreCase(keyword);
        

        List<Student> combined = new ArrayList<>();
        combined.addAll(nameMatches);
        combined.addAll(rollMatches);
        combined.addAll(departmentMatches);
        combined.addAll(emailMatches);

        // Remove duplicates
        return combined.stream()
                .distinct()
                .collect(Collectors.toList());
    }

}
