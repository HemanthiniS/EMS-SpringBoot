package com.example.demo.unit;

import com.example.demo.dto.EmployeeDTO;
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
import static org.junit.jupiter.api.Assertions.assertFalse;  // Add this import
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John Doe");
        employeeDTO.setEmail("john.doe@example.com");

        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");

        when(dtoConverter.toEmployeeEntity(employeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(dtoConverter.toEmployeeDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.saveEmployee(employeeDTO);

        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    public void testGetAllEmployees() {
        Employee employee1 = new Employee();
        employee1.setName("John Doe");
        employee1.setEmail("john.doe@example.com");

        Employee employee2 = new Employee();
        employee2.setName("Jane Doe");
        employee2.setEmail("jane.doe@example.com");

        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.setName("John Doe");
        employeeDTO1.setEmail("john.doe@example.com");

        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setName("Jane Doe");
        employeeDTO2.setEmail("jane.doe@example.com");

        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));
        when(dtoConverter.toEmployeeDTO(employee1)).thenReturn(employeeDTO1);
        when(dtoConverter.toEmployeeDTO(employee2)).thenReturn(employeeDTO2);

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertTrue(result.contains(employeeDTO1));
        assertTrue(result.contains(employeeDTO2));
    }

    @Test
    public void testGetEmployeeById() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employeeId);
        employeeDTO.setName("John Doe");
        employeeDTO.setEmail("john.doe@example.com");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(dtoConverter.toEmployeeDTO(employee)).thenReturn(employeeDTO);

        Optional<EmployeeDTO> result = employeeService.getEmployeeById(employeeId);

        assertTrue(result.isPresent());
        assertEquals(employeeDTO, result.get());
    }

    @Test
    public void testDeleteEmployee() {
        // Setup
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName("John Doe");

        // Mock behavior
        when(employeeRepository.existsById(employeeId)).thenReturn(true);

        // Act
        employeeService.deleteEmployee(employeeId);

        // Assert
        verify(employeeRepository).deleteById(employeeId);
        // Verify that the employee was indeed deleted
        when(employeeRepository.existsById(employeeId)).thenReturn(false);
        assertFalse(employeeRepository.existsById(employeeId));
    }

}
