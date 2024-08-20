import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DepartmentServiceTest {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveDepartment() {
        Department department = new Department();
        department.setName("Human Resources");

        // Mock the repository save behavior
        when(departmentRepository.save(department)).thenReturn(department);

        // Call the service method
        Department result = departmentService.saveDepartment(department);

        // Verify the result
        assertEquals("Human Resources", result.getName());
    }

    @Test
    public void testGetAllDepartments() {
        Department department1 = new Department();
        department1.setName("Human Resources");

        Department department2 = new Department();
        department2.setName("IT");

        // Mock the repository behavior
        when(departmentRepository.findAll()).thenReturn(List.of(department1, department2));

        // Call the service method
        List<Department> result = departmentService.getAllDepartments();

        // Verify the result
        assertEquals(2, result.size());
        assertTrue(result.contains(department1));
        assertTrue(result.contains(department2));
    }

    @Test
    public void testGetDepartmentById() {
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);
        department.setName("Human Resources");

        // Mock the repository behavior
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        // Call the service method
        Optional<Department> result = departmentService.getDepartmentById(departmentId);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(department, result.get());
    }

    @Test
    public void testDeleteDepartment() {
        Long departmentId = 1L;

        // Mock the repository behavior
        when(departmentRepository.existsById(departmentId)).thenReturn(true);

        // Call the service method
        departmentService.deleteDepartment(departmentId);

        // Verify that the repository's delete method was called with the correct ID
        verify(departmentRepository).deleteById(departmentId);
    }

    @Test
    public void testDeleteDepartment_NotFound() {
        Long departmentId = 1L;

        // Mock the repository behavior
        when(departmentRepository.existsById(departmentId)).thenReturn(false);

        // Expect a RuntimeException to be thrown
        try {
            departmentService.deleteDepartment(departmentId);
        } catch (RuntimeException e) {
            assertEquals("Department not found with ID: " + departmentId, e.getMessage());
        }
    }

    @Test
    public void testSaveDepartment_NullDepartment() {
        // Expect an IllegalArgumentException to be thrown
        try {
            departmentService.saveDepartment(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Department cannot be null", e.getMessage());
        }
    }

    @Test
    public void testGetDepartmentById_NullId() {
        // Expect an IllegalArgumentException to be thrown
        try {
            departmentService.getDepartmentById(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Department ID cannot be null", e.getMessage());
        }
    }

    @Test
    public void testDeleteDepartment_NullId() {
        // Expect an IllegalArgumentException to be thrown
        try {
            departmentService.deleteDepartment(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Department ID cannot be null", e.getMessage());
        }
    }
}

