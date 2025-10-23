package com.pragma.vc.tracker.knowledgeapplication.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "\"Applied_Knowledge\"")
public class JpaAppliedKnowledgeEntity {

    @Id
    @Column(name = "\"id\"")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"project_id\"", nullable = false)
    private Long projectId;

    @Column(name = "\"pragmatic_id\"", nullable = false)
    private Long pragmaticId;

    @Column(name = "\"knowledge_id\"")
    private Long knowledgeId;

    @Column(name = "\"knowledge_level\"")
    private Long knowledgeLevel;

    @Column(name = "\"startDate\"")
    private LocalDate startDate;

    public JpaAppliedKnowledgeEntity() {
    }

    public JpaAppliedKnowledgeEntity(Long projectId, Long pragmaticId, Long knowledgeId, Long knowledgeLevel, LocalDate startDate) {
        this.projectId = projectId;
        this.pragmaticId = pragmaticId;
        this.knowledgeId = knowledgeId;
        this.knowledgeLevel = knowledgeLevel;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getPragmaticId() {
        return pragmaticId;
    }

    public void setPragmaticId(Long pragmaticId) {
        this.pragmaticId = pragmaticId;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Long getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public void setKnowledgeLevel(Long knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}