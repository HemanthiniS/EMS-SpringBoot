package com.example.demo.unit;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.converter.DtoConverter;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DtoConverter dtoConverter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveEmployee() {
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");

        // Mock the behavior of dtoConverter
        when(employeeRepository.save(employee)).thenReturn(employee);

        // Call the service method
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // Verify the result
        assertEquals("John Doe", savedEmployee.getName());
        assertEquals("john.doe@example.com", savedEmployee.getEmail());
    }

    @Test
    public void testGetAllEmployees() {
        Employee employee1 = new Employee();
        employee1.setName("John Doe");
        employee1.setEmail("john.doe@example.com");

        Employee employee2 = new Employee();
        employee2.setName("Jane Doe");
        employee2.setEmail("jane.doe@example.com");

        // Mock the behavior of the repository
        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));

        // Call the service method
        List<Employee> result = employeeService.getAllEmployees();

        // Verify the result
        assertEquals(2, result.size());
        assertTrue(result.contains(employee1));
        assertTrue(result.contains(employee2));
    }

    @Test
    public void testGetEmployeeById() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");

        // Mock the behavior of the repository
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Call the service method
        Optional<Employee> result = employeeService.getEmployeeById(employeeId);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
    }

    @Test
    public void testDeleteEmployee() {
        Long employeeId = 1L;

        // Mock the repository behavior
        when(employeeRepository.existsById(employeeId)).thenReturn(true);

        // Call the service method
        employeeService.deleteEmployee(employeeId);

        // Verify that the repository's delete method was called
        verify(employeeRepository).deleteById(employeeId);
        // Verify that the employee was indeed deleted
        when(employeeRepository.existsById(employeeId)).thenReturn(false);
        assertFalse(employeeRepository.existsById(employeeId));
    }

    @Test
    public void testSaveEmployee_NullEmployee() {
        // Expect an IllegalArgumentException to be thrown
        try {
            employeeService.saveEmployee(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Employee cannot be null", e.getMessage());
        }
    }

    @Test
    public void testGetEmployeeById_NullId() {
        // Expect an IllegalArgumentException to be thrown
        try {
            employeeService.getEmployeeById(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Employee ID cannot be null", e.getMessage());
        }
    }

    @Test
    public void testDeleteEmployee_NullId() {
        // Expect an IllegalArgumentException to be thrown
        try {
            employeeService.deleteEmployee(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Employee ID cannot be null", e.getMessage());
        }
    }
}
