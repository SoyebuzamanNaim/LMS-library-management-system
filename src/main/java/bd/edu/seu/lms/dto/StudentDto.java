package bd.edu.seu.lms.dto;

import bd.edu.seu.lms.model.StudentStatus;
import java.util.List;

public record StudentDto(
                String name,
                String roll,
                String department,
                String email,
                List<String> phones,
                StudentStatus status) {
}
