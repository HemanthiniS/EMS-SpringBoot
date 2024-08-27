package com.example.demo.controller;

import com.example.demo.assembler.DepartmentResourceAssembler;
import com.example.demo.dto.DepartmentDTO;
import com.example.demo.entity.Department;
import com.example.demo.model.DepartmentResourceModel;
import com.example.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Value("${api.key}")
    private String validApiKey;

    private final DepartmentService departmentService;
    private final DepartmentResourceAssembler departmentAssembler;

    public DepartmentController(DepartmentService departmentService, DepartmentResourceAssembler departmentAssembler) {
        this.departmentService = departmentService;
        this.departmentAssembler = departmentAssembler;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDepartment(@RequestBody DepartmentResourceModel departmentResourceModel,
                                                @RequestHeader(value = "API-Key", required = false) String apiKey) {
        if (!isValidApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        try {
            // Convert resource model to entity
            Department department = new Department();
            department.setName(departmentResourceModel.getName());
            departmentService.saveDepartment(department);
            return ResponseEntity.status(HttpStatus.CREATED).body("Department added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding department: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<DepartmentResourceModel>> getAllDepartments(@RequestHeader(value = "API-Key", required = false) String apiKey) {
        if (!isValidApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Department> departments = departmentService.getAllDepartments();
        List<DepartmentResourceModel> departmentResources = departments.stream()
                .map(departmentAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(departmentResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResourceModel> getDepartmentById(@PathVariable Long id,
                                                                     @RequestHeader(value = "API-Key", required = false) String apiKey) {
        if (!isValidApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Department> department = departmentService.getDepartmentById(id);
        return department.map(departmentAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResourceModel> updateDepartment(@PathVariable Long id,
                                                                    @RequestBody DepartmentResourceModel departmentResourceModel,
                                                                    @RequestHeader(value = "API-Key", required = false) String apiKey) {
        if (!isValidApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Department> existingDepartmentOptional = departmentService.getDepartmentById(id);
        if (!existingDepartmentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Department existingDepartment = existingDepartmentOptional.get();
        existingDepartment.setName(departmentResourceModel.getName());

        Department updatedDepartment = departmentService.saveDepartment(existingDepartment);
        DepartmentResourceModel updatedDepartmentResource = departmentAssembler.toModel(updatedDepartment);
        return ResponseEntity.ok(updatedDepartmentResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id,
                                                 @RequestHeader(value = "API-Key", required = false) String apiKey) {
        if (!isValidApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (!departmentService.getDepartmentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dtos")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentDTOs(@RequestHeader(value = "API-Key", required = false) String apiKey) {
        if (!isValidApiKey(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<DepartmentDTO> departmentDTOs = departmentService.getDepartmentDTOs();
        return ResponseEntity.ok(departmentDTOs);
    }

    private boolean isValidApiKey(String apiKey) {
        return validApiKey.equals(apiKey);
    }
}



