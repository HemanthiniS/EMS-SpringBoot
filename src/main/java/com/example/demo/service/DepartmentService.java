package com.example.demo.service;

import com.example.demo.dto.DepartmentDTO;
import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Value("${api.key}") // Assuming the API key is stored in application.properties
    private String validApiKey;

    public boolean isValidToken(String apiKey) {
        return validApiKey.equals(apiKey);
    }

    @Transactional
    public Department saveDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Department> getDepartmentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Department ID cannot be null");
        }
        return departmentRepository.findById(id);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Department ID cannot be null");
        }
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with ID: " + id);
        }
        departmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getDepartmentDTOs() {
        return departmentRepository.findDepartmentDTOs();
    }
}
