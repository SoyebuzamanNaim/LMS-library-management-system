package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Book;
import bd.edu.seu.lms.model.Vendor;
import bd.edu.seu.lms.repository.BookRepo;
import bd.edu.seu.lms.repository.VendorRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorService {
    private final VendorRepo vendorRepo;
    private final BookRepo bookRepo;

    public VendorService(VendorRepo vendorRepo, BookRepo bookRepo) {
        this.vendorRepo = vendorRepo;
        this.bookRepo = bookRepo;
    }

    @Transactional
    public Vendor saveVendor(Vendor vendor) {

        if (vendor.getId() != null && vendorRepo.existsById(vendor.getId())) {
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
        if (!vendorRepo.existsById(id)) {
            throw new IllegalArgumentException("Vendor does not exist");
        }

        List<Book> books = bookRepo.findByVendor_Id(id);

        if (!books.isEmpty()) {
            String middle = "";
            for (Book b : books) {
                middle += (b.getTitle() + ", ");
            }
            String s = "Cannot delete vendor " +
                    middle
                    + " books assigned to this vendor.";
            throw new IllegalArgumentException(s);
        }

        vendorRepo.deleteById(id);
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

        List<Vendor> combined = new ArrayList<>();
        combined.addAll(nameMatches);
        combined.addAll(contactPersonMatches);
        combined.addAll(emailMatches);
        return combined.stream()
                .distinct()
                .collect(Collectors.toList());
    }

}
