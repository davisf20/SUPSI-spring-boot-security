package security.backend.server.service;

import org.springframework.stereotype.Service;
import security.backend.server.model.Customer;
import security.backend.server.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer getById(int id) {
        return customerRepository.findById((long) id).orElse(null);
    }

    public boolean exists(int id) {
        return customerRepository.existsById((long) id);
    }

    public Customer save(Customer customer) {
        if (exists(customer.getCustomerId()))
            return null;
        return customerRepository.save(customer);
    }
}
