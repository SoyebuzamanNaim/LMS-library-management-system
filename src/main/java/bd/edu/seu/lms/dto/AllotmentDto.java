package bd.edu.seu.lms.dto;

import java.time.LocalDate;

public record AllotmentDto(
        Integer studentId,
        Integer bookId,
        LocalDate issueDate,
        LocalDate returnDate,

//        Apply enum
        String status, // Active, Returned
        Double fineAmount) {
}
