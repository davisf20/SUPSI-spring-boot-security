package security.backend.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import security.backend.server.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "SELECT * FROM Employee WHERE username = ?1", nativeQuery = true)
    Employee findByUsername(String username);
}
