package com.example.demo.integration;

import com.example.demo.controller.DepartmentController;
import com.example.demo.dto.DepartmentDTO;
import com.example.demo.entity.Department;
import com.example.demo.service.DepartmentService;
import com.example.demo.converter.DtoConverter;
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
    private DtoConverter dtoConverter;

    @Autowired
    private ObjectMapper objectMapper;

    private DepartmentDTO departmentDTO;
    private Department departmentEntity;

    @BeforeEach
    public void setUp() {
        departmentDTO = new DepartmentDTO();
        departmentDTO.setId(1L);
        departmentDTO.setName("HR Department");

        departmentEntity = new Department();
        departmentEntity.setId(1L);
        departmentEntity.setName("HR Department");

        // Mock the DtoConverter behavior
        when(dtoConverter.toDepartmentDTO(any(Department.class))).thenReturn(departmentDTO);
        when(dtoConverter.toDepartmentEntity(any(DepartmentDTO.class))).thenReturn(departmentEntity);
    }

    @Test
    public void testCreateDepartment() throws Exception {
        when(departmentService.saveDepartment(any(DepartmentDTO.class))).thenReturn(departmentDTO);

        mockMvc.perform(post("/api/departments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Department added successfully"));
    }

    @Test
    public void testGetAllDepartments() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList(departmentDTO));

        mockMvc.perform(get("/api/departments/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))  // Check if there is at least one department
                .andExpect(jsonPath("$[0].id", notNullValue()))  // Check if id is present
                .andExpect(jsonPath("$[0].name", notNullValue()));  // Check if name is present
    }

    @Test
    public void testGetDepartmentById() throws Exception {
        when(departmentService.getDepartmentById(anyLong())).thenReturn(Optional.of(departmentDTO));

        mockMvc.perform(get("/api/departments/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))  // Check if id is present
                .andExpect(jsonPath("$.name", notNullValue()));  // Check if name is present
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        when(departmentService.getDepartmentById(anyLong())).thenReturn(Optional.of(departmentDTO));
        when(departmentService.saveDepartment(any(DepartmentDTO.class))).thenReturn(departmentDTO);

        departmentDTO.setName("Updated Department");

        mockMvc.perform(put("/api/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))  // Check if id is present
                .andExpect(jsonPath("$.name", is("Updated Department")));  // Check if name was updated
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        when(departmentService.getDepartmentById(anyLong())).thenReturn(Optional.of(departmentDTO));

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent());
    }
}
