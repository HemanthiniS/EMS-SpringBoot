package com.example.demo.service;

import com.example.demo.assembler.DepartmentResourceAssembler;
import com.example.demo.converter.DtoConverter;
import com.example.demo.dto.DepartmentDTO;
import com.example.demo.entity.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DtoConverter dtoConverter;

    @Autowired
    private DepartmentResourceAssembler departmentResourceAssembler;

    @Transactional
    public DepartmentDTO saveDepartment(DepartmentDTO departmentDTO) {
        if (departmentDTO == null) {
            throw new IllegalArgumentException("DepartmentDTO cannot be null");
        }
        Department department = dtoConverter.toDepartmentEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return departmentResourceAssembler.toModel(savedDepartment);
    }

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentResourceAssembler::toModel)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<DepartmentDTO> getDepartmentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Department ID cannot be null");
        }
        return departmentRepository.findById(id)
                .map(departmentResourceAssembler::toModel);
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
}
