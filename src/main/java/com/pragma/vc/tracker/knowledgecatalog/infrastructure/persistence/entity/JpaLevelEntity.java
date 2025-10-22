package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "\"Knowledge_Level\"")
public class JpaLevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "attributes", length = 5000)
    private String attributes;

    public JpaLevelEntity() {
    }

    public JpaLevelEntity(Long id, String name, String attributes) {
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}