package com.example.demo.model;

import org.springframework.hateoas.RepresentationModel;

public class EmployeeResourceModel extends RepresentationModel<EmployeeResourceModel> {
    private Long id;
    private String name;
    private String email;
    private Long departmentId; // Add this field

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}

