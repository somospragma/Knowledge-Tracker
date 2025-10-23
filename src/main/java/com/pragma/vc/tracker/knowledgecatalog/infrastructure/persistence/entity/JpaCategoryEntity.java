package com.pragma.vc.tracker.knowledgecatalog.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "\"knowledge_category\"")
public class JpaCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"name\"", nullable = false, unique = true, length = 50)
    private String name;

    public JpaCategoryEntity() {
    }

    public JpaCategoryEntity(Long id, String name) {
        this.id = id;
        this.name = name;
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
}