package bd.edu.seu.lms.controller;

import bd.edu.seu.lms.dto.StudentDto;
import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.model.StudentStatus;
import bd.edu.seu.lms.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

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
        model.addAttribute("search", search);
        model.addAttribute("students", studentService.searchStudents(search));
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("studentdto", new StudentDto("", "", "", "", new ArrayList<>(), StudentStatus.ACTIVE));
        return "students";
    }

    @PostMapping("/students/save")
    public String saveStudent(@ModelAttribute("studentdto") StudentDto studentDto,
            RedirectAttributes redirectAttributes) {
        try {
            if (studentDto.name() == null ) {
                redirectAttributes.addFlashAttribute("error", "Name is required");
                return "redirect:/students";
            }
            if (studentDto.roll() == null ) {
                redirectAttributes.addFlashAttribute("error", "Roll is required");
                return "redirect:/students";
            }
            Student student = new Student();
            student.setName(studentDto.name());
            student.setRoll(studentDto.roll());
            student.setDepartment(studentDto.department());
            student.setEmail(studentDto.email());
            student.setPhones(studentDto.phones() );
            student.setStatus(studentDto.status() );
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("success", "Student added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/students";
    }

    @PostMapping("/students/update")
    public String updateStudent(@ModelAttribute StudentDto studentDto, int id,
            RedirectAttributes redirectAttributes) {
        try {
            if (studentDto.name() == null ) {
                redirectAttributes.addFlashAttribute("error", "Name is required");
                return "redirect:/students";
            }
            if (studentDto.roll() == null ) {
                redirectAttributes.addFlashAttribute("error", "Roll is required");
                return "redirect:/students";
            }
            Student student = studentService.getStudentById(id);
            student.setName(studentDto.name());
            student.setRoll(studentDto.roll());
            student.setDepartment(studentDto.department());
            student.setEmail(studentDto.email());
            student.setPhones(studentDto.phones() );
            student.setStatus(studentDto.status() );
            studentService.updateStudent(student);
            redirectAttributes.addFlashAttribute("success", "Student updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/students";
    }

    @PostMapping("/students/delete")
    public String deleteStudent(int id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("success", "Student deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/students";
    }
}
