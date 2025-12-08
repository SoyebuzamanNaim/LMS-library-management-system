package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Vendor;
import bd.edu.seu.lms.repository.VendorRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class VendorService {
    private final VendorRepo vendorRepo;

    public VendorService(VendorRepo vendorRepo) {
        this.vendorRepo = vendorRepo;
    }

    public Vendor saveVendor(Vendor vendor) {
        return vendorRepo.save(vendor);
    }

    public Vendor updateVendor(int id, Vendor vendor) {
        return vendorRepo.findById(id).map(existing -> {
            existing.setName(vendor.getName());
            existing.setContactPerson(vendor.getContactPerson());
            existing.setEmail(vendor.getEmail());
            existing.setPhone(vendor.getPhone());
            existing.setAddress(vendor.getAddress());
            return vendorRepo.save(existing);
        }).orElse(null);
    }

    public void deleteVendor(int id) {
        if (vendorRepo.existsById(id)) {
            vendorRepo.deleteById(id);
        }
    }

    public ArrayList<Vendor> getAllVendors() {
        return new ArrayList<>(vendorRepo.findAll());
    }

    public Vendor getVendorById(int id) {
        return vendorRepo.findById(id).orElse(null);
    }

    public ArrayList<Vendor> searchVendors(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(vendorRepo.findAll());
        }
        String lowerKeyword = keyword.toLowerCase();
        return vendorRepo.findAll().stream().filter(v -> {
            String name = v.getName() != null ? v.getName().toLowerCase() : "";
            String contactPerson = v.getContactPerson() != null ? v.getContactPerson().toLowerCase() : "";
            String email = v.getEmail() != null ? v.getEmail().toLowerCase() : "";
            String phone = v.getPhone() != null ? v.getPhone().toLowerCase() : "";
            String address = v.getAddress() != null ? v.getAddress().toLowerCase() : "";
            return name.contains(lowerKeyword) || contactPerson.contains(lowerKeyword) || email.contains(lowerKeyword)
                    || phone.contains(lowerKeyword) || address.contains(lowerKeyword);
        }).collect(Collectors.toCollection(ArrayList::new));
    }

}
