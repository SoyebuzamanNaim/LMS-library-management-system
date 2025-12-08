package bd.edu.seu.lms.repository;

import bd.edu.seu.lms.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepo extends JpaRepository<Publication, Integer> {
}
