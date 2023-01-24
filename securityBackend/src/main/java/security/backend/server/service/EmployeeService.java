package security.backend.server.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.backend.server.model.Employee;
import security.backend.server.repository.EmployeeRepository;

@Service
public class EmployeeService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder bcryptEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder bcryptEncoder) {
        this.employeeRepository = employeeRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    /*public Employee save(Employee employee) {
        Employee newEmployee = new Employee();
        newEmployee.setUsername(employee.getUsername());
        newEmployee.setPassword(bcryptEncoder.encode(employee.getPassword()));
        newEmployee.setRole(employee.getRole());
        return employeeRepository.save(newEmployee);
    }*/
}
