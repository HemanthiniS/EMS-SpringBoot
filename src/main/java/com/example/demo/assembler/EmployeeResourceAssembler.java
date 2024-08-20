package com.example.demo.assembler;

import com.example.demo.entity.Employee;
import com.example.demo.model.EmployeeResourceModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class EmployeeResourceAssembler extends RepresentationModelAssemblerSupport<Employee, EmployeeResourceModel> {

    public EmployeeResourceAssembler() {
        super(Employee.class, EmployeeResourceModel.class);
    }

    @Override
    public EmployeeResourceModel toModel(Employee employee) {
        EmployeeResourceModel resource = new EmployeeResourceModel();
        resource.setId(employee.getId());
        resource.setName(employee.getName());
        resource.setEmail(employee.getEmail());
        // departmentId is set in the DepartmentResourceAssembler
        return resource;
    }
}
