package bd.edu.seu.lms.dto;

import bd.edu.seu.lms.model.SubscriptionType;

import bd.edu.seu.lms.model.SubscriptionStatus;

public record SubscriptionDto(
                Integer studentId,
                SubscriptionType type,

                SubscriptionStatus status) {
}
