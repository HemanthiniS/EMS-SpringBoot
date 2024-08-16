package com.example.demo.service;

import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Project;
import com.example.demo.converter.DtoConverter;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.assembler.ProjectResourceAssembler; // Import the assembler
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DtoConverter dtoConverter;

    @Autowired
    private ProjectResourceAssembler projectResourceAssembler; // Inject the assembler

    private static final int MIN_AGE = 21;
    private static final int MAX_AGE = 35;

    public ProjectDTO saveProject(ProjectDTO projectDTO) {
        Project project = dtoConverter.toProjectEntity(projectDTO);

        // Validate employee's age
        int employeeAge = project.getAge(); // Fetch age from project itself
        if (employeeAge < MIN_AGE || employeeAge > MAX_AGE) {
            throw new IllegalArgumentException("Employee must be between " + MIN_AGE + " and " + MAX_AGE + " years old to work on this project.");
        }

        // Save the project if the employee's age is valid
        Project savedProject = projectRepository.save(project);
        return projectResourceAssembler.toModel(savedProject); // Use assembler to get DTO with HATEOAS links
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectResourceAssembler::toModel) // Convert entities to DTOs with HATEOAS links
                .toList();
    }

    public Optional<ProjectDTO> getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(projectResourceAssembler::toModel); // Convert entity to DTO with HATEOAS links
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}

