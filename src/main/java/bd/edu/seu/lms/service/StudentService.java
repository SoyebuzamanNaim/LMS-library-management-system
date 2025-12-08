package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.repository.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepo studentRepo;

    public StudentService(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Student saveStudent(Student student) {
        if (student.getStatus() == null || student.getStatus().trim().isEmpty()) {
            student.setStatus("Active");
        }
        return studentRepo.save(student);
    }

    public Student updateStudent(int id, Student student) {
        return studentRepo.findById(id).map(existing -> {
            existing.setName(student.getName());
            existing.setRoll(student.getRoll());
            existing.setDepartment(student.getDepartment());
            existing.setEmail(student.getEmail());
            existing.setPhone(student.getPhone());
            existing.setStatus(
                    student.getStatus() != null && !student.getStatus().trim().isEmpty() ? student.getStatus()
                            : "Active");
            return studentRepo.save(existing);
        }).orElse(null);
    }

    public void deleteStudent(int id) {
        if (studentRepo.existsById(id)) {
            studentRepo.deleteById(id);
        }
    }

    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(studentRepo.findAll());
    }

    public Student getStudentById(int id) {
        return studentRepo.findById(id).orElse(null);
    }

    public Student getStudentByRoll(String roll) {
        return studentRepo.findAll().stream().filter(s -> s.getRoll() != null && s.getRoll().equals(roll)).findFirst()
                .orElse(null);
    }

    public ArrayList<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(studentRepo.findAll());
        }
        String lowerKeyword = keyword.toLowerCase();
        return studentRepo.findAll().stream().filter(s -> {
            String name = s.getName() != null ? s.getName().toLowerCase() : "";
            String roll = s.getRoll() != null ? s.getRoll().toLowerCase() : "";
            String department = s.getDepartment() != null ? s.getDepartment().toLowerCase() : "";
            String email = s.getEmail() != null ? s.getEmail().toLowerCase() : "";
            String phone = s.getPhone() != null ? s.getPhone().toLowerCase() : "";
            return name.contains(lowerKeyword) || roll.contains(lowerKeyword) || department.contains(lowerKeyword)
                    || email.contains(lowerKeyword) || phone.contains(lowerKeyword);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

}
