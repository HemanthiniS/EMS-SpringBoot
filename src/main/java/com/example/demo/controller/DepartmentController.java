package com.example.demo.controller;

import com.example.demo.assembler.DepartmentResourceAssembler;
import com.example.demo.entity.Department;
import com.example.demo.model.DepartmentResourceModel;
import com.example.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentResourceAssembler departmentAssembler;

    @PostMapping("/add")
    public ResponseEntity<String> addDepartment(@RequestBody DepartmentResourceModel departmentResourceModel) {
        try {
            // Convert resource model to entity
            Department department = new Department();
            department.setId(departmentResourceModel.getId());
            department.setName(departmentResourceModel.getName());
            departmentService.saveDepartment(department);
            return ResponseEntity.status(HttpStatus.CREATED).body("Department added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding department: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<DepartmentResourceModel>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        List<DepartmentResourceModel> departmentResources = departments.stream()
                .map(departmentAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(departmentResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResourceModel> getDepartmentById(@PathVariable Long id) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        return department.map(departmentAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResourceModel> updateDepartment(@PathVariable Long id, @RequestBody DepartmentResourceModel departmentResourceModel) {
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
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (!departmentService.getDepartmentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}


