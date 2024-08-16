
package com.example.demo.assembler;

import com.example.demo.controller.EmployeeController;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeResourceAssembler extends RepresentationModelAssemblerSupport<Employee, EmployeeDTO> {

    public EmployeeResourceAssembler() {
        super(EmployeeController.class, EmployeeDTO.class);
    }

    @Override
    public EmployeeDTO toModel(Employee employee) {
        // Create a new EmployeeDTO and copy properties from the Employee entity
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto);

        // Add self-link to the DTO for HATEOAS compliance
        dto.add(linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel());

        return dto;
    }
}
