package security.backend.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import security.backend.server.model.Employee;
import security.backend.server.repository.EmployeeRepository;
import security.backend.server.service.EmployeeService;

import java.util.List;

@SpringBootApplication
public class securityBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(securityBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner addUsername(EmployeeService employeeService) {
        return args -> {
            List<Employee> employees = employeeService.getAll();
            for(Employee e : employees) {
                if (e.getUsername() == null || e.getUsername().isEmpty()) {
                    e.setUsername(e.getFirstName().toLowerCase() + "." + e.getLastName().toLowerCase());
                    employeeService.save(e);
                }
            }
        };
    }

    @Bean
    public CommandLineRunner addPassword(EmployeeRepository employeeRepository, PasswordEncoder bcryptEncoder) {
        return args -> {
            List<Employee> employees = employeeRepository.findAll();
            for(Employee e : employees) {
                if (e.getPassword() == null || e.getPassword().isEmpty()) {
                    e.setPassword(bcryptEncoder.encode("Jo5hu4!"));
                    employeeRepository.save(e);
                }
            }
        };
    }

    @Bean
    public CommandLineRunner addRole(EmployeeService employeeService) {
        return args -> {
            List<Employee> employees = employeeService.getAll();
            for(Employee e : employees) {
                if (e.getRole() == null || e.getRole().isEmpty()) {
                    if (e.getTitle().contains("Manager"))
                        e.setRole("ROLE_ADMIN");
                    else
                        e.setRole("ROLE_USER");
                    employeeService.save(e);
                }
            }
        };
    }
}
