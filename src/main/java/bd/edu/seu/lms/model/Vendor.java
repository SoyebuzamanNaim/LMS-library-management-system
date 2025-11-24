package bd.edu.seu.lms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {
    private String id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
}
