package bd.edu.seu.lms.repository;

import bd.edu.seu.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    java.util.Optional<User> findByEmail(String email);
}
