package bd.edu.seu.lms.dto;

public record BookDto(
                String title,
                String author,
                Integer publicationId,
                Integer vendorId,
                String category,
                int totalCopies,
                int availableCopies,
                Double pricePerCopy,
                String status,
                String description) {
}
