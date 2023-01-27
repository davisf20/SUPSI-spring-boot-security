package security.backend.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.backend.server.model.Customer;
import security.backend.server.model.Employee;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> getCustomersBySupportRepId(Employee supportRepId);
}
