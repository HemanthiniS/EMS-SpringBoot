package com.example.demo.dto;

import org.springframework.hateoas.RepresentationModel;

public class DepartmentDTO extends RepresentationModel<DepartmentDTO> {

    private Long id;
    private String name;

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
}
