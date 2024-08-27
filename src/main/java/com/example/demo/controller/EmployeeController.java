package com.example.demo.controller;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.assembler.EmployeeResourceAssembler;
import com.example.demo.entity.Department;
import com.example.demo.model.EmployeeResourceModel;
import com.example.demo.entity.Employee;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeResourceAssembler employeeAssembler;

    private static final String USERNAME = "hema";
    private static final String PASSWORD = "12345";

    private boolean isAuthenticated(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);
            return USERNAME.equals(values[0]) && PASSWORD.equals(values[1]);
        }
        return false;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeResourceModel employeeResourceModel, HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            if (employeeResourceModel.getDepartmentId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Department ID must be provided");
            }

            Department department = departmentService.getDepartmentById(employeeResourceModel.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            Employee employee = new Employee();
            employee.setId(employeeResourceModel.getId());
            employee.setName(employeeResourceModel.getName());
            employee.setEmail(employeeResourceModel.getEmail());
            employee.setDepartment(department);

            employeeService.saveEmployee(employee);

            return ResponseEntity.status(HttpStatus.CREATED).body("Employee added successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding employee: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResourceModel>> getAllEmployees(HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeResourceModel> employeeResources = employees.stream()
                .map(employeeAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResourceModel> getEmployeeById(@PathVariable Long id, HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(emp -> ResponseEntity.ok(employeeAssembler.toModel(emp)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody EmployeeResourceModel employeeResourceModel, HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Optional<Employee> existingEmployeeOptional = employeeService.getEmployeeById(id);
        if (existingEmployeeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Employee existingEmployee = existingEmployeeOptional.get();
        existingEmployee.setName(employeeResourceModel.getName());
        existingEmployee.setEmail(employeeResourceModel.getEmail());

        employeeService.saveEmployee(existingEmployee);
        return ResponseEntity.ok("Employee updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id, HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!employeeService.getEmployeeById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

}
