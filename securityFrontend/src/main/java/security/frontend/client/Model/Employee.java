package security.frontend.client.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String lastName;
    private String firstName;
    private String username;
    private String password;
    private String title;
    private String role;
    private Employee reportsTo;
    private LocalDateTime birthDate;
    private LocalDateTime hireDate;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
    private String fax;
    private String email;
}
