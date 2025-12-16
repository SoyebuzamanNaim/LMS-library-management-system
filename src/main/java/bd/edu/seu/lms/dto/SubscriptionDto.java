package bd.edu.seu.lms.dto;

import bd.edu.seu.lms.model.SubscriptionType;
import bd.edu.seu.lms.model.Student;
import bd.edu.seu.lms.model.SubscriptionStatus;

public record SubscriptionDto(
                Student student,
                SubscriptionType type,
                SubscriptionStatus status) {    
}
