package com.example.demo.integration;                                                                                             
                                                                                                                                  
import com.example.demo.controller.DepartmentController;                                                                          
import com.example.demo.dto.DepartmentDTO;                                                                                        
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
import java.util.List;                                                                                                            
import java.util.Optional;                                                                                                        
                                                                                                                                  
import static org.hamcrest.Matchers.hasSize;                                                                                      
import static org.hamcrest.Matchers.is;                                                                                           
import static org.mockito.ArgumentMatchers.any;                                                                                   
import static org.mockito.ArgumentMatchers.anyLong;                                                                               
import static org.mockito.Mockito.doNothing;                                                                                      
import static org.mockito.Mockito.when;                                                                                           
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;                                         
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;                                            
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;                                           
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;                                            
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;                                          
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;                                         
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;                                           
                                                                                                                                  
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
                                                                                                                                  
    private static final String API_KEY = "AB12CD34EF56GH78IJ90KL12MN34OP56QR78ST90UVWX12YZ34"; // Same key used in controller     
                                                                                                                                  
    @BeforeEach                                                                                                                   
    public void setUp() {                                                                                                         
        departmentResourceModel = new DepartmentResourceModel();                                                                  
        departmentResourceModel.setId(1L);                                                                                        
        departmentResourceModel.setName("HR Department");                                                                         
                                                                                                                                  
        departmentEntity = new Department();                                                                                      
        departmentEntity.setId(1L);                                                                                               
        departmentEntity.setName("HR Department");                                                                                
                                                                                                                                  
        // Create DepartmentDTO with the correct constructor                                                                      
        List<Long> exampleProjectIds = Arrays.asList(1L); // Example list if required                                             
        DepartmentDTO departmentDTO = new DepartmentDTO(1L, "HR Department", exampleProjectIds);                                  
                                                                                                                                  
        // Mocking service and assembler behavior                                                                                 
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(departmentEntity);                               
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList(departmentEntity));                                  
        when(departmentService.getDepartmentById(anyLong())).thenReturn(Optional.of(departmentEntity));                           
        doNothing().when(departmentService).deleteDepartment(anyLong());                                                          
        when(departmentService.getDepartmentDTOs()).thenReturn(Arrays.asList(departmentDTO));                                     
                                                                                                                                  
        when(departmentAssembler.toModel(any(Department.class))).thenReturn(departmentResourceModel);                             
    }                                                                                                                             
                                                                                                                                  
    @Test                                                                                                                         
    public void testCreateDepartment() throws Exception {                                                                         
        mockMvc.perform(post("/api/departments/add")                                                                              
                        .header("API-Key", API_KEY)                                                                               
                        .contentType(MediaType.APPLICATION_JSON)                                                                  
                        .content(objectMapper.writeValueAsString(departmentResourceModel)))                                       
                .andExpect(status().isCreated())                                                                                  
                .andExpect(content().string("Department added successfully"));                                                    
    }                                                                                                                             
                                                                                                                                  
    @Test                                                                                                                         
    public void testGetAllDepartments() throws Exception {                                                                        
        mockMvc.perform(get("/api/departments/all")                                                                               
                        .header("API-Key", API_KEY)                                                                               
                        .accept(MediaType.APPLICATION_JSON))                                                                      
                .andExpect(status().isOk())                                                                                       
                .andExpect(jsonPath("$", hasSize(1)))                                                                             
                .andExpect(jsonPath("$[0].id", is(1)))                                                                            
                .andExpect(jsonPath("$[0].name", is("HR Department")));                                                           
    }                                                                                                                             
                                                                                                                                  
    @Test                                                                                                                         
    public void testGetDepartmentById() throws Exception {                                                                        
        mockMvc.perform(get("/api/departments/1")                                                                                 
                        .header("API-Key", API_KEY)                                                                               
                        .accept(MediaType.APPLICATION_JSON))                                                                      
                .andExpect(status().isOk())                                                                                       
                .andExpect(jsonPath("$.id", is(1)))                                                                               
                .andExpect(jsonPath("$.name", is("HR Department")));                                                              
    }                                                                                                                             
                                                                                                                                  
    @Test                                                                                                                         
    public void testUpdateDepartment() throws Exception {                                                                         
        departmentResourceModel.setName("Updated Department");                                                                    
                                                                                                                                  
        Department updatedDepartmentEntity = new Department();                                                                    
        updatedDepartmentEntity.setId(1L);                                                                                        
        updatedDepartmentEntity.setName("Updated Department");                                                                    
                                                                                                                                  
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(updatedDepartmentEntity);                        
                                                                                                                                  
        mockMvc.perform(put("/api/departments/1")                                                                                 
                        .header("API-Key", API_KEY)                                                                               
                        .contentType(MediaType.APPLICATION_JSON)                                                                  
                        .content(objectMapper.writeValueAsString(departmentResourceModel)))                                       
                .andExpect(status().isOk())                                                                                       
                .andExpect(jsonPath("$.id", is(1)))                                                                               
                .andExpect(jsonPath("$.name", is("Updated Department")));                                                         
    }                                                                                                                             
                                                                                                                                  
    @Test                                                                                                                         
    public void testDeleteDepartment() throws Exception {                                                                         
        mockMvc.perform(delete("/api/departments/1")                                                                              
                        .header("API-Key", API_KEY))                                                                              
                .andExpect(status().isNoContent());                                                                               
    }                                                                                                                             
                                                                                                                                  
    @Test                                                                                                                         
    public void testGetDepartmentDTOs() throws Exception {                                                                        
        mockMvc.perform(get("/api/departments/dtos")                                                                              
                        .header("API-Key", API_KEY)                                                                               
                        .accept(MediaType.APPLICATION_JSON))                                                                      
                .andExpect(status().isOk())                                                                                       
                .andExpect(jsonPath("$[0].id", is(1)))                                                                            
                .andExpect(jsonPath("$[0].name", is("HR Department")));                                                           
    }                                                                                                                             
                                                                                                                                  
    @Test                                                                                                                         
    public void testUnauthorizedAccess() throws Exception {                                                                       
        mockMvc.perform(get("/api/departments/all")                                                                               
                        .header("API-Key", "invalid_api_key")                                                                     
                        .accept(MediaType.APPLICATION_JSON))                                                                      
                .andExpect(status().isUnauthorized())                                                                             
                .andExpect(content().string("Invalid API Key"));                                                                  
    }                                                                                                                             
}                                                                                                                                 
                                                                                                                                  
