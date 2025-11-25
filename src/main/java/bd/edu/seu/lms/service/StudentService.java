package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final ArrayList<Student> students = new ArrayList<>();
    private long ind = 1;

    public void saveStudent(Student student) {
        if (student.getName() == null || student.getName().trim().equals("")) {
            throw new IllegalArgumentException("Name is required");
        }
        if (student.getRoll() == null || student.getRoll().trim().equals("")) {
            throw new IllegalArgumentException("Roll number is required");
        }
        if (student.getEmail() == null || student.getEmail().trim().equals("")) {
            throw new IllegalArgumentException("Email is required");
        }
        if (student.getPhone() == null || student.getPhone().trim().equals("")) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (student.getStatus() == null || student.getStatus().trim().equals("")) {
            throw new IllegalArgumentException("Status is required");
        }
        if (student.getId() != null && students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            throw new IllegalArgumentException("Student with this id already exists");
        }
        if (student.getRoll() != null && students.stream()
                .anyMatch(s -> s.getRoll() != null && s.getRoll().equalsIgnoreCase(student.getRoll()))) {
            throw new IllegalArgumentException("Student with this roll number already exists");
        }
        if (student.getEmail() != null && students.stream()
                .anyMatch(s -> s.getEmail() != null && s.getEmail().equalsIgnoreCase(student.getEmail()))) {
            throw new IllegalArgumentException("Student with this email already exists");
        }
        student.setId(Long.toString(ind++));
        if (student.getStatus() == null || student.getStatus().trim().equals("")) {
            student.setStatus("Active");
        }
        students.add(student);
    }

    public void updateStudent(String id, Student student) {
        if (student.getName() == null || student.getName().trim().equals("")) {
            throw new IllegalArgumentException("Name is required");
        }
        if (student.getRoll() == null || student.getRoll().trim().equals("")) {
            throw new IllegalArgumentException("Roll number is required");
        }
        if (student.getDepartment() == null || student.getDepartment().trim().equals("")) {
            throw new IllegalArgumentException("Department is required");
        }
        if (student.getEmail() == null || student.getEmail().trim().equals("")) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!students.stream().anyMatch(s -> s.getId().equals(id))) {
            throw new IllegalArgumentException("Student not found");
        }
        if (student.getRoll() != null && students.stream()
                .anyMatch(s -> s.getRoll() != null && s.getRoll().equalsIgnoreCase(student.getRoll()))) {
            throw new IllegalArgumentException("Student with this roll number already exists");
        }
        if (student.getEmail() != null && students.stream()
                .anyMatch(s -> s.getEmail() != null && s.getEmail().equalsIgnoreCase(student.getEmail()))) {
            throw new IllegalArgumentException("Student with this email already exists");
        }
        students.stream().filter(s -> s.getId().equals(id)).findFirst().ifPresent(s -> {
            s.setName(student.getName());
            s.setRoll(student.getRoll());
            s.setDepartment(student.getDepartment());
            s.setEmail(student.getEmail());
            s.setPhone(student.getPhone());
            s.setStatus(student.getStatus() != null && !student.getStatus().trim().equals("") ? student.getStatus()
                    : "Active");
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

    public StudentService() {
        initializeDummyData();
    }

    private void initializeDummyData() {
        Student s1 = new Student();
        s1.setId(Long.toString(ind++));
        s1.setName("Soyebuzaman Naim");
        s1.setRoll("2023200000066");
        s1.setDepartment("Computer Science");
        s1.setEmail("soyebuzamannaim@gmail.com");
        s1.setPhone("015XXXXXXXX");
        s1.setStatus("Active");
        students.add(s1);

        Student s2 = new Student();
        s2.setId(Long.toString(ind++));
        s2.setName("Jihad Mia");
        s2.setRoll("2023200000505");
        s2.setDepartment("Computer Science");
        s2.setEmail("2023200000505@seu.edu.bd");
        s2.setPhone("015XXXXXXXX");
        s2.setStatus("Inactive");
        students.add(s2);

        Student s3 = new Student();
        s3.setId(Long.toString(ind++));
        s3.setName("Chulbul Pandey");
        s3.setRoll("2023200000505");
        s3.setDepartment("Computer Science");
        s3.setEmail("chulbulpandey@gmail.com");
        s3.setPhone("015XXXXXXXX");
        s3.setStatus("Active");
        students.add(s3);

        Student s4 = new Student();
        s4.setId(Long.toString(ind++));
        s4.setName("Kazi Samir");
        s4.setRoll("2023200000661");
        s4.setDepartment("Computer Science");
        s4.setEmail("kazisamir@gmail.com");
        s4.setPhone("015XXXXXXXX");
        s4.setStatus("Active");
        students.add(s4);

        Student s5 = new Student();
        s5.setId(Long.toString(ind++));
        s5.setName("David Googens");
        s5.setRoll("2023200000772");
        s5.setDepartment("Physics");
        s5.setEmail("2023200000772@seu.edu.bd");
        s5.setPhone("015XXXXXXXX");
        s5.setStatus("Inactive");
        students.add(s5);
    }
}
