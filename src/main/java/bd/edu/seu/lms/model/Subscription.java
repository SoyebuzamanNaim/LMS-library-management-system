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
public class Subscription {
    private String id;
    private String studentId;
    private String type; // Premium, Standard, Basic
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // Active, Expired
}
