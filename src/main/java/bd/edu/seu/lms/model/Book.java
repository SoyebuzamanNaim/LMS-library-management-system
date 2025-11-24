package bd.edu.seu.lms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id;
    private String title;
    private String author;
    private String publicationId;
    private String vendorId;
    private String category;
    private Integer totalCopies;
    private Integer availableCopies;
    private Double pricePerCopy;
    private String status; // Available, Unavailable
    private String description;
}
