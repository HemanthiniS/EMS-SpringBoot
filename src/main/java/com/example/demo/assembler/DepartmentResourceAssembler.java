package com.example.demo.assembler;

import com.example.demo.entity.Department;
import com.example.demo.model.DepartmentResourceModel;
import com.example.demo.model.EmployeeResourceModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentResourceAssembler extends RepresentationModelAssemblerSupport<Department, DepartmentResourceModel> {

    private final EmployeeResourceAssembler employeeAssembler;

    public DepartmentResourceAssembler(EmployeeResourceAssembler employeeAssembler) {
        super(Department.class, DepartmentResourceModel.class);
        this.employeeAssembler = employeeAssembler;
    }

    @Override
    public DepartmentResourceModel toModel(Department department) {
        DepartmentResourceModel resource = new DepartmentResourceModel();
        resource.setId(department.getId());
        resource.setName(department.getName());

        List<EmployeeResourceModel> employeeResources = department.getEmployees().stream()
                .map(employee -> {
                    EmployeeResourceModel employeeResource = employeeAssembler.toModel(employee);
                    employeeResource.setDepartmentId(department.getId());  // Set the departmentId
                    return employeeResource;
                })
                .collect(Collectors.toList());
        resource.setEmployees(employeeResources);

        return resource;
    }
}

