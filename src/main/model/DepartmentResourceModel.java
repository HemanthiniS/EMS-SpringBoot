package com.example.demo.model;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class DepartmentResourceModel extends RepresentationModel<DepartmentResourceModel> {
    private Long id;
    private String name;
    private List<EmployeeResourceModel> employees;

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

    public List<EmployeeResourceModel> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeResourceModel> employees) {
        this.employees = employees;
    }
}
