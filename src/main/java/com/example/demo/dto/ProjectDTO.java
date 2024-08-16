package com.example.demo.dto;

import org.springframework.hateoas.RepresentationModel;

public class ProjectDTO extends RepresentationModel<ProjectDTO> {
    private Long id;
    private String name;
    private Long employeeId;
    private Integer age; // Add the age field

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getAge() {
        return age; // Getter for age
    }

    public void setAge(Integer age) {
        this.age = age; // Setter for age
    }
}

