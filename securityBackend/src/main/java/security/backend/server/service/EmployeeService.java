package security.backend.server.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.backend.server.model.Employee;
import security.backend.server.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder bcryptEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder bcryptEncoder) {
        this.employeeRepository = employeeRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public boolean exists(Long id) {
        return employeeRepository.existsById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles;
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null)
            throw new UsernameNotFoundException("User not found with the name " + username);

        roles = List.of(new SimpleGrantedAuthority(employee.getRole()));
        return new User(employee.getUsername(), employee.getPassword(), roles);
    }

    public Employee save(Employee employee) {
        employee.setPassword(bcryptEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }
}
