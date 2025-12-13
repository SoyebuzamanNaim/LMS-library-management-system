package bd.edu.seu.lms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Allotment {
    private String id;
    private String studentId;
    private String bookId;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private String status; // Active, Returned
    private Double fineAmount;

}
