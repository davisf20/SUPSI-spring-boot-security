package security.backend.server.service;

import org.springframework.stereotype.Service;
import security.backend.server.model.Customer;
import security.backend.server.model.Employee;
import security.backend.server.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomers(Employee employee) {
        if (employee.getRole().equals("ROLE_ADMIN")) {
            return customerRepository.findAll();
        }

        return customerRepository.getCustomersBySupportRepId(employee);
    }

    public Customer getById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public boolean exists(Long id) {
        return customerRepository.existsById(id);
    }

    public Customer save(Customer customer) {
        if (exists(customer.getCustomerId()))
            return null;
        return customerRepository.save(customer);
    }
}
