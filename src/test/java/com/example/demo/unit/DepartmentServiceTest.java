
package com.example.demo.unit;

import com.example.demo.dto.DepartmentDTO;
import com.example.demo.entity.Department;
import com.example.demo.converter.DtoConverter;
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

    @Mock
    private DtoConverter dtoConverter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveDepartment() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Human Resources");

        Department department = new Department();
        department.setName("Human Resources");

        when(dtoConverter.toDepartmentEntity(departmentDTO)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(dtoConverter.toDepartmentDTO(department)).thenReturn(departmentDTO);

        DepartmentDTO result = departmentService.saveDepartment(departmentDTO);

        assertEquals("Human Resources", result.getName());
    }

    @Test
    public void testGetAllDepartments() {
        Department department1 = new Department();
        department1.setName("Human Resources");

        Department department2 = new Department();
        department2.setName("IT");

        DepartmentDTO departmentDTO1 = new DepartmentDTO();
        departmentDTO1.setName("Human Resources");

        DepartmentDTO departmentDTO2 = new DepartmentDTO();
        departmentDTO2.setName("IT");

        when(departmentRepository.findAll()).thenReturn(List.of(department1, department2));
        when(dtoConverter.toDepartmentDTO(department1)).thenReturn(departmentDTO1);
        when(dtoConverter.toDepartmentDTO(department2)).thenReturn(departmentDTO2);

        List<DepartmentDTO> result = departmentService.getAllDepartments();

        assertEquals(2, result.size());
        assertTrue(result.contains(departmentDTO1));
        assertTrue(result.contains(departmentDTO2));
    }

    @Test
    public void testGetDepartmentById() {
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);
        department.setName("Human Resources");

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(departmentId);
        departmentDTO.setName("Human Resources");

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(dtoConverter.toDepartmentDTO(department)).thenReturn(departmentDTO);

        Optional<DepartmentDTO> result = departmentService.getDepartmentById(departmentId);

        assertTrue(result.isPresent());
        assertEquals(departmentDTO, result.get());
    }

    @Test
    public void testDeleteDepartment() {
        Long departmentId = 1L;

        // Mock the behavior of the repository to indicate that the department exists
        when(departmentRepository.existsById(departmentId)).thenReturn(true);

        // Call the delete method
        departmentService.deleteDepartment(departmentId);

        // Verify that deleteById was called with the correct ID
        verify(departmentRepository).deleteById(departmentId);
    }
}
