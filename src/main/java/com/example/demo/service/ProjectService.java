package com.example.demo.service;

import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Project;
import com.example.demo.converter.DtoConverter;
import com.example.demo.repository.ProjectRepository;
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

    public ProjectDTO saveProject(ProjectDTO projectDTO) {
        Project project = dtoConverter.toProjectEntity(projectDTO);
        Project savedProject = projectRepository.save(project);
        return dtoConverter.toProjectDTO(savedProject);
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(dtoConverter::toProjectDTO)
                .toList();
    }

    public Optional<ProjectDTO> getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(dtoConverter::toProjectDTO);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
