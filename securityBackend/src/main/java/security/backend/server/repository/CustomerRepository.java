package security.backend.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.backend.server.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
