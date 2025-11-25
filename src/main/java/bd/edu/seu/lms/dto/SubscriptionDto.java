package bd.edu.seu.lms.dto;

import java.time.LocalDate;

public record SubscriptionDto(
        String studentId,
        String type,
        LocalDate startDate,
        LocalDate endDate,
        String status) {
}
