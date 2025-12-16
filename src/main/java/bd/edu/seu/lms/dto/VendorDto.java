package bd.edu.seu.lms.dto;

import java.util.List;

public record VendorDto(String name, String contactPerson, String email, List<String> phones, String address) {
}
