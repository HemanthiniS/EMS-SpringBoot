package com.example.demo.integration;

import com.example.demo.controller.ProjectController;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.service.ProjectService;
import com.example.demo.converter.DtoConverter;
import com.example.demo.entity.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProjectController.class)
public class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private DtoConverter dtoConverter;

    @Autowired
    private ObjectMapper objectMapper;

    private ProjectDTO projectDTO;

    @BeforeEach
    public void setUp() {
        projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setName("Project Alpha");
        projectDTO.setEmployeeId(1L); // Updated field

        // Mock the DtoConverter behavior
        when(dtoConverter.toProjectDTO(any())).thenReturn(projectDTO);
        when(dtoConverter.toProjectEntity(any())).thenReturn(new Project());
    }

    @Test
    public void testCreateProject() throws Exception {
        when(projectService.saveProject(any(ProjectDTO.class))).thenReturn(projectDTO);

        mockMvc.perform(post("/api/projects/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Project Alpha")))
                .andExpect(jsonPath("$.employeeId", is(1)));
    }

    @Test
    public void testGetAllProjects() throws Exception {
        when(projectService.getAllProjects()).thenReturn(Arrays.asList(projectDTO));

        mockMvc.perform(get("/api/projects/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Project Alpha")))
                .andExpect(jsonPath("$[0].employeeId", is(1)));
    }

    @Test
    public void testGetProjectById() throws Exception {
        when(projectService.getProjectById(anyLong())).thenReturn(Optional.of(projectDTO));

        mockMvc.perform(get("/api/projects/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Project Alpha")))
                .andExpect(jsonPath("$.employeeId", is(1)));
    }

    @Test
    public void testUpdateProject() throws Exception {
        when(projectService.getProjectById(anyLong())).thenReturn(Optional.of(projectDTO));
        when(projectService.saveProject(any(ProjectDTO.class))).thenReturn(projectDTO);

        projectDTO.setName("Updated Project Alpha"); // Updated field

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Project Alpha")))
                .andExpect(jsonPath("$.employeeId", is(1)));
    }

    @Test
    public void testDeleteProject() throws Exception {
        when(projectService.getProjectById(anyLong())).thenReturn(Optional.of(projectDTO));

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
    }
}
