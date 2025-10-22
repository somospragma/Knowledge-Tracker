package com.pragma.vc.tracker.knowledgecatalog.application.dto;

import java.util.Map;

public class LevelDTO {
    private Long id;
    private String name;
    private Map<String, String> attributes;

    public LevelDTO() {
    }

    public LevelDTO(Long id, String name, Map<String, String> attributes) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
    }

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

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
