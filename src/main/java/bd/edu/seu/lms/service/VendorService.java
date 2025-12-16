package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Vendor;
import bd.edu.seu.lms.repository.VendorRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorService {
    private final VendorRepo vendorRepo;

    public VendorService(VendorRepo vendorRepo) {
        this.vendorRepo = vendorRepo;
    }
    @Transactional
    public Vendor saveVendor(Vendor vendor) {
        if (vendorRepo.existsById(vendor.getId())) {
            throw new IllegalArgumentException("Vendor already exist");
        }
        return vendorRepo.save(vendor);
    }

    @Transactional
    public Vendor updateVendor(Vendor vendor) {
        if (vendor.getId() == null || !vendorRepo.existsById(vendor.getId())) {
            throw new IllegalArgumentException("Vendor does not exist");
        }
        return vendorRepo.save(vendor);
    }

    @Transactional
    public void deleteVendor(int id) {
        try {
            vendorRepo.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Vendor does not exist");
        }
    }

    public List<Vendor> getAllVendors() {
        return vendorRepo.findAll();
    }

    public Vendor getVendorById(int id) {
        return vendorRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Vendor does not exist"));
    }

    public List<Vendor> searchVendors(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return vendorRepo.findAll();
        }
        // isfai
        List<Vendor> nameMatches = vendorRepo.findByNameContainingIgnoreCase(keyword);
        List<Vendor> contactPersonMatches = vendorRepo.findByContactPersonContainingIgnoreCase(keyword);
        List<Vendor> emailMatches = vendorRepo.findByEmailContainingIgnoreCase(keyword);
        List<Vendor> phoneMatches = vendorRepo.findByPhoneContainingIgnoreCase(keyword);

        List<Vendor> combined = new ArrayList<>();
        combined.addAll(nameMatches);
        combined.addAll(contactPersonMatches);
        combined.addAll(emailMatches);
        combined.addAll(phoneMatches);
        return combined.stream()
                .distinct()
                .collect(Collectors.toList());
    }

}
