package bd.edu.seu.lms.dto;

import java.time.LocalDate;

public record AllotmentDto(
        String studentId,
        String bookId,
        LocalDate issueDate,
        LocalDate returnDate,

//        Apply enum
        String status, // Active, Returned
        Double fineAmount) {
}
