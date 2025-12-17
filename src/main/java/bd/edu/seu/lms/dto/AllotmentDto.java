package bd.edu.seu.lms.dto;

import bd.edu.seu.lms.model.AllotmentStatus;
import java.time.LocalDate;

public record AllotmentDto(
        Integer studentId,
        Integer bookId,
        LocalDate issueDate,
        AllotmentStatus status,
        Double fineAmount) {
}
