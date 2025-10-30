package com.pragma.vc.tracker.projectmanagement.application.dto;

/**
 * Data Transfer Object for Territory
 */
public class TerritoryDTO {
    private Long id;
    private String name;

    public TerritoryDTO() {
    }

    public TerritoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
