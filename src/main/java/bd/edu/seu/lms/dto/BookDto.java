package bd.edu.seu.lms.dto;

import bd.edu.seu.lms.model.BookStatus;

public record BookDto(
        String title,
        String author,
        Integer publicationId,
        Integer vendorId,
        String category,
        int totalCopies,
        int availableCopies,
        Double pricePerCopy,
        BookStatus status,
        String description) {
}
