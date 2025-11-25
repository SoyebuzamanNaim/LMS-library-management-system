package bd.edu.seu.lms.dto;

public record BookDto(
                String title,
                String author,
                String publicationId,
                String vendorId,
                String category,
                Integer totalCopies,
                Integer availableCopies,
                Double pricePerCopy,
                String status,
                String description) {
}
