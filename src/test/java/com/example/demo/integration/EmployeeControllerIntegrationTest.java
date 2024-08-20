package com.example.demo.integration;

import com.example.demo.Demo6Application;
import com.example.demo.assembler.EmployeeResourceAssembler;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.model.EmployeeResourceModel;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Demo6Application.class)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private EmployeeResourceAssembler employeeAssembler;

    private MockMvc mockMvc;
    private Employee employee;
    private EmployeeResourceModel employeeResourceModel;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Department department = new Department();
        department.setId(1L);
        department.setName("HR");

        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment(department);

        employeeResourceModel = new EmployeeResourceModel();
        employeeResourceModel.setId(1L);
        employeeResourceModel.setName("John Doe");
        employeeResourceModel.setEmail("john.doe@example.com");
        employeeResourceModel.setDepartmentId(1L);

        Mockito.when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));
        Mockito.when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(employee));
        Mockito.when(employeeAssembler.toModel(employee)).thenReturn(employeeResourceModel);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Mockito.when(employeeService.saveEmployee(Mockito.any(Employee.class))).thenReturn(employee);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"departmentId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee updated successfully")); // Expect success message
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Mockito.when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent()); // Expect 204 No Content

        Mockito.verify(employeeService, Mockito.times(1)).deleteEmployee(1L);
    }
}
