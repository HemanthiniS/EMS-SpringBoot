package com.example.demo.integration;

import com.example.demo.controller.DepartmentController;
import com.example.demo.entity.Department;
import com.example.demo.model.DepartmentResourceModel;
import com.example.demo.service.DepartmentService;
import com.example.demo.assembler.DepartmentResourceAssembler;
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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private DepartmentResourceAssembler departmentAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private DepartmentResourceModel departmentResourceModel;
    private Department departmentEntity;

    @BeforeEach
    public void setUp() {
        departmentResourceModel = new DepartmentResourceModel();
        departmentResourceModel.setId(1L);
        departmentResourceModel.setName("HR Department");

        departmentEntity = new Department();
        departmentEntity.setId(1L);
        departmentEntity.setName("HR Department");

        // Mock the behavior of departmentService
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(departmentEntity);
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList(departmentEntity));
        when(departmentService.getDepartmentById(anyLong())).thenReturn(Optional.of(departmentEntity));
        doNothing().when(departmentService).deleteDepartment(anyLong()); // Adjust mock

        // Mock the behavior of departmentAssembler
        when(departmentAssembler.toModel(any(Department.class))).thenReturn(departmentResourceModel);
    }

    @Test
    public void testCreateDepartment() throws Exception {
        mockMvc.perform(post("/api/departments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentResourceModel)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Department added successfully"));
    }

    @Test
    public void testGetAllDepartments() throws Exception {
        mockMvc.perform(get("/api/departments/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))  // Check if there is at least one department
                .andExpect(jsonPath("$[0].id", is(1)))  // Check if ID is correct
                .andExpect(jsonPath("$[0].name", is("HR Department")));  // Check if name is correct
    }

    @Test
    public void testGetDepartmentById() throws Exception {
        mockMvc.perform(get("/api/departments/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))  // Check if ID is correct
                .andExpect(jsonPath("$.name", is("HR Department")));  // Check if name is correct
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        departmentResourceModel.setName("Updated Department");

        // Mock the updated entity
        Department updatedDepartmentEntity = new Department();
        updatedDepartmentEntity.setId(1L);
        updatedDepartmentEntity.setName("Updated Department");

        when(departmentService.saveDepartment(any(Department.class))).thenReturn(updatedDepartmentEntity);

        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentResourceModel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))  // Check if ID is correct
                .andExpect(jsonPath("$.name", is("Updated Department")));  // Check if name was updated
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent());
    }
}

