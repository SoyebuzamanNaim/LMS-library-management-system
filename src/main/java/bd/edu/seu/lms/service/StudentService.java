package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final ArrayList<Student> students = new ArrayList<>();
    private long ind = 1;

    public StudentService() {
        initializeDummyData();
    }

    private void initializeDummyData() {
        Student s1 = new Student();
        s1.setId(Long.toString(ind++));
        s1.setName("Johnathan Doe");
        s1.setRoll("2023001");
        s1.setDepartment("Computer Science");
        s1.setEmail("john.doe@uni.edu");
        s1.setPhone("+1 555-0199");
        s1.setStatus("Active");
        students.add(s1);

        Student s2 = new Student();
        s2.setId(Long.toString(ind++));
        s2.setName("Emily Blunt");
        s2.setRoll("2023002");
        s2.setDepartment("History");
        s2.setEmail("emily.b@uni.edu");
        s2.setPhone("+1 555-2244");
        s2.setStatus("Suspended");
        students.add(s2);

        Student s3 = new Student();
        s3.setId(Long.toString(ind++));
        s3.setName("Michael Chen");
        s3.setRoll("2023003");
        s3.setDepartment("Mathematics");
        s3.setEmail("michael.chen@uni.edu");
        s3.setPhone("+1 555-3355");
        s3.setStatus("Active");
        students.add(s3);

        Student s4 = new Student();
        s4.setId(Long.toString(ind++));
        s4.setName("Sarah Johnson");
        s4.setRoll("2023004");
        s4.setDepartment("Physics");
        s4.setEmail("sarah.j@uni.edu");
        s4.setPhone("+1 555-4466");
        s4.setStatus("Active");
        students.add(s4);

        Student s5 = new Student();
        s5.setId(Long.toString(ind++));
        s5.setName("David Wilson");
        s5.setRoll("2023005");
        s5.setDepartment("Chemistry");
        s5.setEmail("david.w@uni.edu");
        s5.setPhone("+1 555-5577");
        s5.setStatus("Active");
        students.add(s5);
    }

    public void saveStudent(Student student) {
        if (student.getId() != null && students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            throw new IllegalArgumentException("Student already exists");
        }
        if (student.getRoll() != null && students.stream().anyMatch(s ->
                s.getRoll() != null && s.getRoll().equalsIgnoreCase(student.getRoll()))) {
            throw new IllegalArgumentException("Student with this roll number already exists");
        }
        if (student.getEmail() != null && students.stream().anyMatch(s ->
                s.getEmail() != null && s.getEmail().equalsIgnoreCase(student.getEmail()))) {
            throw new IllegalArgumentException("Student with this email already exists");
        }
        student.setId(Long.toString(ind++));
        if (student.getStatus() == null || student.getStatus().trim().equals("")) {
            student.setStatus("Active");
        }
        students.add(student);
    }

    public void updateStudent(String id, Student student) {
        if (!students.stream().anyMatch(s -> s.getId().equals(id))) {
            throw new IllegalArgumentException("Student not found");
        }
        if (student.getRoll() != null && students.stream().anyMatch(s ->
                !s.getId().equals(id) && s.getRoll() != null && s.getRoll().equalsIgnoreCase(student.getRoll()))) {
            throw new IllegalArgumentException("Student with this roll number already exists");
        }
        if (student.getEmail() != null && students.stream().anyMatch(s ->
                !s.getId().equals(id) && s.getEmail() != null && s.getEmail().equalsIgnoreCase(student.getEmail()))) {
            throw new IllegalArgumentException("Student with this email already exists");
        }
        students.stream().filter(s -> s.getId().equals(id)).findFirst().ifPresent(s -> {
            s.setName(student.getName());
            s.setRoll(student.getRoll());
            s.setDepartment(student.getDepartment());
            s.setEmail(student.getEmail());
            s.setPhone(student.getPhone());
            s.setStatus(student.getStatus() != null && !student.getStatus().trim().equals("") ? student.getStatus() : "Active");
        });
    }

    public void deleteStudent(String id) {
        if (!students.stream().anyMatch(s -> s.getId().equals(id))) {
            throw new IllegalArgumentException("Student not found");
        }
        students.removeIf(s -> s.getId().equals(id));
    }

    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student getStudentById(String id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public ArrayList<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(students);
        }
        String lowerKeyword = keyword.toLowerCase();
        return students.stream().filter(s -> {
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

