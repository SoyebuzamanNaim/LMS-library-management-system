package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Allotment;
import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.repository.AllotmentRepo;
import bd.edu.seu.lms.repository.StudentRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepo studentRepo;
    private final AllotmentRepo allotmentRepo;

    public StudentService(StudentRepo studentRepo, AllotmentRepo allotmentRepo) {
        this.studentRepo = studentRepo;
        this.allotmentRepo = allotmentRepo;
    }

    @Transactional
    public Student saveStudent(Student student) {
        if (student.getId() != null && studentRepo.existsById(student.getId())) {
            throw new IllegalArgumentException("Student already exists");
        }
        if (studentRepo.existsByRoll(student.getRoll().trim())) {
            throw new IllegalArgumentException("Roll already exists");
        }
        return studentRepo.save(student);
    }

    @Transactional
    public Student updateStudent(Student student) {
        if (student.getId() == null || !studentRepo.existsById(student.getId())) {
            throw new IllegalArgumentException("Student does not exist");
        }
        return studentRepo.save(student);
    }

    @Transactional
    public void deleteStudent(int id) {
        if (!studentRepo.existsById(id)) {
            throw new IllegalArgumentException("Student does not exist");
        }

        List<Allotment> allotments = allotmentRepo.findByStudent_Id(id);

        if (!allotments.isEmpty()) {
            String middle = "";
            for (Allotment a : allotments) {
                middle += (a.getBook().getTitle() + " to " + a.getStudent().getId() + ", ");
            }
            String s = "Cannot delete allotments of student " +
                    middle
                    + ".";
            throw new IllegalArgumentException(s);
        }

        studentRepo.deleteById(id);
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
        // isfai
        List<Student> nameMatches = studentRepo.findByNameContainingIgnoreCase(keyword);
        List<Student> rollMatches = studentRepo.findByRollContainingIgnoreCase(keyword);
        List<Student> departmentMatches = studentRepo.findByDepartmentContainingIgnoreCase(keyword);
        List<Student> emailMatches = studentRepo.findByEmailContainingIgnoreCase(keyword);

        List<Student> combined = new ArrayList<>();
        combined.addAll(nameMatches);
        combined.addAll(rollMatches);
        combined.addAll(departmentMatches);
        combined.addAll(emailMatches);

        return combined.stream()
                .distinct()
                .collect(Collectors.toList());
    }

}
