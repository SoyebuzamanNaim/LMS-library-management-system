package bd.edu.seu.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    SubscriptionStatus status;
    @Enumerated(EnumType.STRING)
    SubscriptionType type;
    @OneToOne(mappedBy = "subscription", fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;
}
