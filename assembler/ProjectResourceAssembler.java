
package com.example.demo.assembler;

import com.example.demo.controller.ProjectController;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Project;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectResourceAssembler extends RepresentationModelAssemblerSupport<Project, ProjectDTO> {

    public ProjectResourceAssembler() {
        super(ProjectController.class, ProjectDTO.class);
    }

    @Override
    public ProjectDTO toModel(Project project) {
        // Create a new ProjectDTO and copy properties from the Project entity
        ProjectDTO dto = new ProjectDTO();
        BeanUtils.copyProperties(project, dto);

        // Add self-link to the DTO for HATEOAS compliance
        dto.add(linkTo(methodOn(ProjectController.class).getProjectById(project.getId())).withSelfRel());

        return dto;
    }
}
