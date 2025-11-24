package bd.edu.seu.lms.service;

import bd.edu.seu.lms.model.Vendor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class VendorService {
    private final ArrayList<Vendor> vendors = new ArrayList<>();
    private long ind = 1;

    public void saveVendor(Vendor vendor) {
        if (vendors.stream().anyMatch(v -> v.getId().equals(vendor.getId()))) {
            throw new IllegalArgumentException("Vendor already exists");
        }
        vendor.setId(Long.toString(ind++));
        vendors.add(vendor);
    }

    public void updateVendor(String id, Vendor vendor) {
        if (!vendors.stream().anyMatch(v -> v.getId().equals(id))) {
            throw new IllegalArgumentException("Vendor not found");
        }
        vendors.stream().filter(v -> v.getId().equals(id)).findFirst().ifPresent(v -> v.setName(vendor.getName()));
        vendors.stream().filter(v -> v.getId().equals(id)).findFirst()
                .ifPresent(v -> v.setContactPerson(vendor.getContactPerson()));
        vendors.stream().filter(v -> v.getId().equals(id)).findFirst().ifPresent(v -> v.setEmail(vendor.getEmail()));
        vendors.stream().filter(v -> v.getId().equals(id)).findFirst().ifPresent(v -> v.setPhone(vendor.getPhone()));
        vendors.stream().filter(v -> v.getId().equals(id)).findFirst()
                .ifPresent(v -> v.setAddress(vendor.getAddress()));
    }

    public void deleteVendor(String id) {
        if (!vendors.stream().anyMatch(v -> v.getId().equals(id))) {
            throw new IllegalArgumentException("Vendor not found");
        }
        vendors.removeIf(v -> v.getId().equals(id));
    }

    public ArrayList<Vendor> getAllVendors() {
        return new ArrayList<>(vendors);
    }

    public Vendor getVendorById(String id) {
        return vendors.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
    }

    public ArrayList<Vendor> searchVendors(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>(vendors);
        }
        String lowerKeyword = keyword.toLowerCase();
        return vendors.stream().filter(v -> {
            String name = v.getName() != null ? v.getName().toLowerCase() : "";
            String contactPerson = v.getContactPerson() != null ? v.getContactPerson().toLowerCase() : "";
            String email = v.getEmail() != null ? v.getEmail().toLowerCase() : "";
            String phone = v.getPhone() != null ? v.getPhone().toLowerCase() : "";
            String address = v.getAddress() != null ? v.getAddress().toLowerCase() : "";
            return name.contains(lowerKeyword) || contactPerson.contains(lowerKeyword) || email.contains(lowerKeyword)
                    || phone.contains(lowerKeyword) || address.contains(lowerKeyword);
        }).collect(Collectors.toCollection(ArrayList::new));
    }


    // Initialize with dummy data
    public VendorService() {
        initializeDummyData();
    }

    private void initializeDummyData() {
        Vendor v1 = new Vendor();
        v1.setId(Long.toString(ind++));
        v1.setName("Shikkha Prokashoni");
        v1.setContactPerson("Mr. A.K.M. Abdul Mannan");
        v1.setEmail("akmabdulmannan@gmail.com");
        v1.setPhone("015XXXXXXXX");
        v1.setAddress("Dhaka, Bangladesh");
        vendors.add(v1);

        Vendor v2 = new Vendor();
        v2.setId(Long.toString(ind++));
        v2.setName("EduTech Resources");
        v2.setContactPerson("Ms. Sareen Islam");
        v2.setEmail("sareenislam@gmail.com");
        v2.setPhone("015XXXXXXXX");
        v2.setAddress("Dhaka, Bangladesh");
        vendors.add(v2);

        Vendor v3 = new Vendor();
        v3.setId(Long.toString(ind++));
        v3.setName("Academic Book House");
        v3.setContactPerson("Dr. Md. Atahar Hossain");
        v3.setEmail("ataharhossain@gmail.com");
        v3.setPhone("015XXXXXXXX");
        v3.setAddress("Dhaka, Bangladesh");
        vendors.add(v3);

        Vendor v4 = new Vendor();
        v4.setId(Long.toString(ind++));
        v4.setName("Bangladesh Book House");
        v4.setContactPerson("Mr. Md. Ashikur Rahman");
        v4.setEmail("bangladeshbookhouse@gmail.com");
        v4.setPhone("015XXXXXXXX");
        v4.setAddress("Dhaka, Bangladesh");
        vendors.add(v4);

        Vendor v5 = new Vendor();
        v5.setId(Long.toString(ind++));
        v5.setName("BookWorld International");
        v5.setContactPerson("Mr. Md. Ashikur Rahman");
        v5.setEmail("ashikurrahman@gmail.com");
        v5.setPhone("015XXXXXXXX");
        v5.setAddress("Dhaka, Bangladesh");
        vendors.add(v5);
    }

}
