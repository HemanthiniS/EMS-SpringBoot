package com.example.demo.integration;

import com.example.demo.controller.ProjectController;
import com.example.demo.entity.Project;
import com.example.demo.model.ProjectResourceModel;
import com.example.demo.service.ProjectService;
import com.example.demo.assembler.ProjectResourceAssembler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
    private ProjectResourceAssembler projectResourceAssembler;

    private static final String VALID_TOKEN = "valid_token";

    @Autowired
    private ObjectMapper objectMapper;

    private Project project;
    private ProjectResourceModel projectResourceModel;

    @BeforeEach
    public void setUp() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2024-01-01");
        Date endDate = sdf.parse("2024-12-31");

        project = new Project();
        project.setId(1L);
        project.setName("Project Alpha");
        project.setStartDate(startDate);
        project.setEndDate(endDate);

        projectResourceModel = new ProjectResourceModel();
        projectResourceModel.setId(1L);
        projectResourceModel.setName("Project Alpha");
        projectResourceModel.setStartDate(startDate);
        projectResourceModel.setEndDate(endDate);

        when(projectService.saveProject(any(Project.class))).thenReturn(project);
        when(projectService.getProjectById(anyLong())).thenReturn(Optional.of(project));
        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project));
        when(projectResourceAssembler.toModel(any(Project.class))).thenReturn(projectResourceModel);
    }

    @Test
    public void testCreateProject() throws Exception {
        mockMvc.perform(post("/api/projects/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Project Alpha")));
    }

    @Test
    public void testGetAllProjects() throws Exception {
        mockMvc.perform(get("/api/projects/all")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Project Alpha")));
    }

    @Test
    public void testGetProjectById() throws Exception {
        mockMvc.perform(get("/api/projects/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Project Alpha")));
    }

    @Test
    public void testUpdateProject() throws Exception {
        Project updatedProject = new Project();
        updatedProject.setId(1L);
        updatedProject.setName("Updated Project Alpha");
        updatedProject.setStartDate(project.getStartDate());
        updatedProject.setEndDate(project.getEndDate());

        ProjectResourceModel updatedResourceModel = new ProjectResourceModel();
        updatedResourceModel.setId(1L);
        updatedResourceModel.setName("Updated Project Alpha");
        updatedResourceModel.setStartDate(project.getStartDate());
        updatedResourceModel.setEndDate(project.getEndDate());

        when(projectService.saveProject(any(Project.class))).thenReturn(updatedProject);
        when(projectResourceAssembler.toModel(any(Project.class))).thenReturn(updatedResourceModel);

        mockMvc.perform(put("/api/projects/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Project Alpha")));
    }

    @Test
    public void testDeleteProject() throws Exception {
        mockMvc.perform(delete("/api/projects/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(post("/api/projects/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isUnauthorized());
    }
}
