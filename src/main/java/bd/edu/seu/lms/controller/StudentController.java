package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.StudentDto;
import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String students(HttpSession session, Model model, String search) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        if (search != null && !search.trim().equals("")) {
            model.addAttribute("search", search);
            model.addAttribute("students", studentService.searchStudents(search));
        } else {
            model.addAttribute("students", studentService.getAllStudents());
        }
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("studentdto", new StudentDto("", "", "", "", "", "Active"));
        return "students";
    }

    @PostMapping("/students/save")
    public String saveStudent(@ModelAttribute("studentdto") StudentDto studentDto,
            RedirectAttributes redirectAttributes) {
        try {

            Student student = new Student();
            student.setName(studentDto.name());
            student.setRoll(studentDto.roll());
            student.setDepartment(studentDto.department());
            student.setEmail(studentDto.email());
            student.setPhone(studentDto.phone());
            student.setStatus(studentDto.status());
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("success", "Student added successfully");
            return "redirect:/students";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/students";
        }
    }

    @PostMapping("/students/update")
    public String updateStudent(@ModelAttribute StudentDto studentDto, String id,
            RedirectAttributes redirectAttributes) {
        try {
            if (id == null || id.trim().equals("")) {
                throw new IllegalArgumentException("Student ID is required");
            }

            Student student = new Student();
            student.setName(studentDto.name());
            student.setRoll(studentDto.roll());
            student.setDepartment(studentDto.department());
            student.setEmail(studentDto.email());
            student.setPhone(studentDto.phone() != null ? studentDto.phone() : "");
            student.setStatus(
                    studentDto.status() != null && !studentDto.status().trim().equals("") ? studentDto.status()
                            : "Active");
            studentService.updateStudent(id, student);
            redirectAttributes.addFlashAttribute("success", "Student updated successfully");
            return "redirect:/students";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/students";
        }
    }

    @PostMapping("/students/delete")
    public String deleteStudent(String id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("success", "Student deleted successfully");
            return "redirect:/students";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/students";
        }
    }
}
