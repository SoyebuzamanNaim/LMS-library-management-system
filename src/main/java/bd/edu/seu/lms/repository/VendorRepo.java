package bd.edu.seu.lms.repository;

import bd.edu.seu.lms.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepo extends JpaRepository<Vendor, Integer> {
    List<Vendor> findByNameContainingIgnoreCase(String name);
   List<Vendor> findByContactPersonContainingIgnoreCase(String contactPerson);
   List<Vendor> findByEmailContainingIgnoreCase(String email);
   List<Vendor> findByPhoneContainingIgnoreCase(String phone);
  
}
