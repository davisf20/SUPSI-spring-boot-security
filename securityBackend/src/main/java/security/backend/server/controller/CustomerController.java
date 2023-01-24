package security.backend.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import security.backend.server.model.Customer;
import security.backend.server.service.CustomerService;

import java.util.List;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {
        if (customerService.exists(id)) {
            return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/customersByEmployee/{id}")
    public List<Customer> getCustomersByEmployee(@PathVariable int id) {
        return customerService.findAll().stream().filter(customer -> customer.getSupportRepId().getEmployeeId() == id).toList();
    }
}
