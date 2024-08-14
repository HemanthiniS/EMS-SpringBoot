package com.example.demo.unit;

import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Project;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.converter.DtoConverter;
import com.example.demo.service.ProjectService;
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

public class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private Employee employee; // Mock the Employee entity

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveProject() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Project A");
        projectDTO.setEmployeeId(1L); // Only set employeeId here

        Project project = new Project();
        project.setName("Project A");

        when(dtoConverter.toProjectEntity(projectDTO)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(dtoConverter.toProjectDTO(project)).thenReturn(projectDTO);

        ProjectDTO result = projectService.saveProject(projectDTO);

        assertEquals("Project A", result.getName());
    }

    @Test
    public void testGetAllProjects() {
        Project project1 = new Project();
        project1.setName("Project A");

        Project project2 = new Project();
        project2.setName("Project B");

        ProjectDTO projectDTO1 = new ProjectDTO();
        projectDTO1.setName("Project A");

        ProjectDTO projectDTO2 = new ProjectDTO();
        projectDTO2.setName("Project B");

        when(projectRepository.findAll()).thenReturn(List.of(project1, project2));
        when(dtoConverter.toProjectDTO(project1)).thenReturn(projectDTO1);
        when(dtoConverter.toProjectDTO(project2)).thenReturn(projectDTO2);

        List<ProjectDTO> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        assertTrue(result.contains(projectDTO1));
        assertTrue(result.contains(projectDTO2));
    }

    @Test
    public void testGetProjectById() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Project A");

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectId);
        projectDTO.setName("Project A");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(dtoConverter.toProjectDTO(project)).thenReturn(projectDTO);

        Optional<ProjectDTO> result = projectService.getProjectById(projectId);

        assertTrue(result.isPresent());
        assertEquals(projectDTO, result.get());
    }

    @Test
    public void testDeleteProject() {
        Long projectId = 1L;

        // No assertions needed for delete method, just verify that it calls deleteById
        projectService.deleteProject(projectId);

        // Verify that deleteById was called with the correct ID
        verify(projectRepository).deleteById(projectId);
    }
}
