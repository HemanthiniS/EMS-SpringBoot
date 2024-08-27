package com.example.demo.repository;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT new com.example.demo.dto.EmployeeDTO(e.id, e.name, YEAR(p.startDate), MONTH(p.startDate), COUNT(p.id)) " +
            "FROM Employee e JOIN Project p ON e.id = p.employee.id " +
            "GROUP BY e.id, e.name, YEAR(p.startDate), MONTH(p.startDate) " +
            "ORDER BY e.id, YEAR(p.startDate), MONTH(p.startDate)")
    List<EmployeeDTO> findHistoricalEmployeeEngagement();

    Optional<Employee> findByUsername(String username); // Add this method
}
