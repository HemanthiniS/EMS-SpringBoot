package com.example.demo.service;

import com.example.demo.assembler.EmployeeResourceAssembler;
import com.example.demo.converter.DtoConverter;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DtoConverter dtoConverter;

    @Autowired
    private EmployeeResourceAssembler employeeResourceAssembler;

    @Transactional
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            throw new IllegalArgumentException("EmployeeDTO cannot be null");
        }

        // Convert DTO to entity
        Employee employee = dtoConverter.toEmployeeEntity(employeeDTO);

        // Save employee and return DTO
        Employee savedEmployee = employeeRepository.save(employee);

        // Return HATEOAS-enabled DTO
        return employeeResourceAssembler.toModel(savedEmployee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeResourceAssembler::toModel)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        return employeeRepository.findById(id)
                .map(employeeResourceAssembler::toModel);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
    }
}
