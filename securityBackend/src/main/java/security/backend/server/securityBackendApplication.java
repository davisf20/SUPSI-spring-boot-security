package security.backend.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import security.backend.server.model.Employee;
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
                if (e.getUsername().isEmpty()) {
                    e.setUsername(e.getFirstName().toLowerCase() + "." + e.getLastName().toLowerCase());
                    employeeService.save(e);
                }
            }
        };
    }

    @Bean
    public CommandLineRunner addPassword(EmployeeService employeeService) {
        return args -> {
            List<Employee> employees = employeeService.getAll();
            for(Employee e : employees) {
                if (e.getPassword().isEmpty()) {
                    e.setPassword("Jo5hu4!");
                    employeeService.save(e);
                }
            }
        };
    }
}
