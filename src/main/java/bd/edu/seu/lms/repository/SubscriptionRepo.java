package bd.edu.seu.lms.repository;

import bd.edu.seu.lms.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepo extends JpaRepository<Subscription, Integer> {
    List<Subscription> findByStudent_NameContainingIgnoreCase(String name);
}
   