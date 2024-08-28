package com.example.demo.controller;

import com.example.demo.entity.Project;
import com.example.demo.model.ProjectResourceModel;
import com.example.demo.service.ProjectService;
import com.example.demo.assembler.ProjectResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectResourceAssembler projectResourceAssembler;

    @PostMapping("/add")
    public ResponseEntity<ProjectResourceModel> addProject(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Valid @RequestBody Project project) {

        // Extract and validate the OAuth 2.0 token from the header
        String token = extractTokenFromHeader(authorizationHeader);
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Project savedProject = projectService.saveProject(project);
            ProjectResourceModel resourceModel = projectResourceAssembler.toModel(savedProject);
            return ResponseEntity.status(HttpStatus.CREATED).body(resourceModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectResourceModel>> getAllProjects(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        // Extract and validate the OAuth 2.0 token from the header
        String token = extractTokenFromHeader(authorizationHeader);
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Project> projects = projectService.getAllProjects();
        List<ProjectResourceModel> resources = projects.stream()
                .map(projectResourceAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResourceModel> getProjectById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id) {

        // Extract and validate the OAuth 2.0 token from the header
        String token = extractTokenFromHeader(authorizationHeader);
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Project> project = projectService.getProjectById(id);
        return project.map(projectResourceAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResourceModel> updateProject(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id,
            @Valid @RequestBody Project project) {

        // Extract and validate the OAuth 2.0 token from the header
        String token = extractTokenFromHeader(authorizationHeader);
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!projectService.getProjectById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        project.setId(id); // Ensure the ID is set for the update
        Project updatedProject = projectService.saveProject(project);
        ProjectResourceModel resourceModel = projectResourceAssembler.toModel(updatedProject);
        return ResponseEntity.ok(resourceModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long id) {

        // Extract and validate the OAuth 2.0 token from the header
        String token = extractTokenFromHeader(authorizationHeader);
        if (!isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!projectService.getProjectById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private boolean isValidToken(String token) {
        // Implement your token validation logic here.
        // For demo purposes, this method should check if the token is valid.
        // In a real-world application, you would verify the token against an authorization server.
        return token != null && token.equals("valid_token"); // Placeholder logic
    }
}

