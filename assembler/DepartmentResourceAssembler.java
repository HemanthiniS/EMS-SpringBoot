package com.example.demo.assembler;

import com.example.demo.controller.DepartmentController;
import com.example.demo.dto.DepartmentDTO;
import com.example.demo.entity.Department;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DepartmentResourceAssembler extends RepresentationModelAssemblerSupport<Department, DepartmentDTO> {

    public DepartmentResourceAssembler() {
        super(DepartmentController.class, DepartmentDTO.class);
    }

    @Override
    public DepartmentDTO toModel(Department department) {
        // Create a new DepartmentDTO and copy properties from the Department entity
        DepartmentDTO dto = new DepartmentDTO();
        BeanUtils.copyProperties(department, dto);

        // Add self-link to the DTO for HATEOAS compliance
        dto.add(linkTo(methodOn(DepartmentController.class).getDepartmentById(department.getId())).withSelfRel());

        return dto;
    }
}
