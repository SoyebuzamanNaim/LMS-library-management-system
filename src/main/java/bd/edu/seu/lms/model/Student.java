package bd.edu.seu.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String roll;
    private String department;
    private String email;
    @ElementCollection
    @CollectionTable(name = "student_phone", joinColumns = @JoinColumn(name = "student_id"))
    private List<String> phones;
    @Enumerated(EnumType.STRING)
    private StudentStatus status;
    // owning side
    @OneToOne(fetch = FetchType.EAGER)

    private Subscription subscription;

}