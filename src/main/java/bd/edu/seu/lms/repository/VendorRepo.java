package bd.edu.seu.lms.repository;

import bd.edu.seu.lms.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepo extends JpaRepository<Vendor, Integer> {
}
