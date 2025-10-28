package com.pragma.vc.tracker.knowledgeapplication.application.usecase;

import com.pragma.vc.tracker.knowledgeapplication.application.dto.AppliedKnowledgeDTO;
import com.pragma.vc.tracker.knowledgeapplication.application.dto.CreateAppliedKnowledgeCommand;
import com.pragma.vc.tracker.knowledgeapplication.application.dto.UpdateAppliedKnowledgeCommand;
import com.pragma.vc.tracker.knowledgeapplication.application.mapper.AppliedKnowledgeMapper;
import com.pragma.vc.tracker.knowledgeapplication.domain.exception.AppliedKnowledgeNotFoundException;
import com.pragma.vc.tracker.knowledgeapplication.domain.model.AppliedKnowledge;
import com.pragma.vc.tracker.knowledgeapplication.domain.model.AppliedKnowledgeId;
import com.pragma.vc.tracker.knowledgeapplication.domain.repository.AppliedKnowledgeRepository;
import com.pragma.vc.tracker.projectmanagement.domain.model.ProjectId;
import com.pragma.vc.tracker.projectmanagement.domain.repository.ProjectRepository;
import com.pragma.vc.tracker.projectmanagement.domain.exception.ProjectNotFoundException;
import com.pragma.vc.tracker.peoplemanagement.domain.model.PragmaticId;
import com.pragma.vc.tracker.peoplemanagement.domain.repository.PragmaticRepository;
import com.pragma.vc.tracker.peoplemanagement.domain.exception.PragmaticNotFoundException;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.KnowledgeId;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.LevelId;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.KnowledgeRepository;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.LevelRepository;
import com.pragma.vc.tracker.knowledgecatalog.domain.exception.KnowledgeNotFoundException;
import com.pragma.vc.tracker.knowledgecatalog.domain.exception.LevelNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class AppliedKnowledgeService {
    private final AppliedKnowledgeRepository appliedKnowledgeRepository;
    private final ProjectRepository projectRepository;
    private final PragmaticRepository pragmaticRepository;
    private final KnowledgeRepository knowledgeRepository;
    private final LevelRepository levelRepository;

    public AppliedKnowledgeService(
            AppliedKnowledgeRepository appliedKnowledgeRepository,
            ProjectRepository projectRepository,
            PragmaticRepository pragmaticRepository,
            KnowledgeRepository knowledgeRepository,
            LevelRepository levelRepository
    ) {
        this.appliedKnowledgeRepository = appliedKnowledgeRepository;
        this.projectRepository = projectRepository;
        this.pragmaticRepository = pragmaticRepository;
        this.knowledgeRepository = knowledgeRepository;
        this.levelRepository = levelRepository;
    }

    public AppliedKnowledgeDTO createAppliedKnowledge(CreateAppliedKnowledgeCommand command) {
        // Validate that referenced entities exist
        ProjectId projectId = ProjectId.of(command.getProjectId());
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        PragmaticId pragmaticId = PragmaticId.of(command.getPragmaticId());
        pragmaticRepository.findById(pragmaticId)
                .orElseThrow(() -> new PragmaticNotFoundException(pragmaticId));

        final KnowledgeId knowledgeId;
        if (command.getKnowledgeId() != null) {
            KnowledgeId knId = KnowledgeId.of(command.getKnowledgeId());
            knowledgeRepository.findById(knId)
                    .orElseThrow(() -> new KnowledgeNotFoundException(knId));
            knowledgeId = knId;
        } else {
            knowledgeId = null;
        }

        final LevelId levelId;
        if (command.getLevelId() != null) {
            LevelId lvlId = LevelId.of(command.getLevelId());
            levelRepository.findById(lvlId)
                    .orElseThrow(() -> new LevelNotFoundException(lvlId));
            levelId = lvlId;
        } else {
            levelId = null;
        }

        AppliedKnowledge appliedKnowledge = AppliedKnowledge.create(
                projectId,
                pragmaticId,
                knowledgeId,
                levelId,
                command.getStartDate()
        );

        AppliedKnowledge saved = appliedKnowledgeRepository.save(appliedKnowledge);
        return AppliedKnowledgeMapper.toDTO(saved);
    }

    public AppliedKnowledgeDTO getAppliedKnowledgeById(Long id) {
        AppliedKnowledge appliedKnowledge = appliedKnowledgeRepository.findById(AppliedKnowledgeId.of(id))
                .orElseThrow(() -> new AppliedKnowledgeNotFoundException(id));
        return AppliedKnowledgeMapper.toDTO(appliedKnowledge);
    }

    public List<AppliedKnowledgeDTO> getAllAppliedKnowledge() {
        return appliedKnowledgeRepository.findAll().stream()
                .map(AppliedKnowledgeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AppliedKnowledgeDTO> getAppliedKnowledgeByProjectId(Long projectId) {
        return appliedKnowledgeRepository.findByProjectId(ProjectId.of(projectId)).stream()
                .map(AppliedKnowledgeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AppliedKnowledgeDTO> getAppliedKnowledgeByPragmaticId(Long pragmaticId) {
        return appliedKnowledgeRepository.findByPragmaticId(PragmaticId.of(pragmaticId)).stream()
                .map(AppliedKnowledgeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AppliedKnowledgeDTO> getAppliedKnowledgeByKnowledgeId(Long knowledgeId) {
        return appliedKnowledgeRepository.findByKnowledgeId(KnowledgeId.of(knowledgeId)).stream()
                .map(AppliedKnowledgeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AppliedKnowledgeDTO> getAppliedKnowledgeByProjectAndPragmatic(Long projectId, Long pragmaticId) {
        return appliedKnowledgeRepository.findByProjectIdAndPragmaticId(
                ProjectId.of(projectId),
                PragmaticId.of(pragmaticId)
        ).stream()
                .map(AppliedKnowledgeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AppliedKnowledgeDTO updateAppliedKnowledge(Long id, UpdateAppliedKnowledgeCommand command) {
        AppliedKnowledge appliedKnowledge = appliedKnowledgeRepository.findById(AppliedKnowledgeId.of(id))
                .orElseThrow(() -> new AppliedKnowledgeNotFoundException(id));

        if (command.getKnowledgeId() != null) {
            KnowledgeId knowledgeId = KnowledgeId.of(command.getKnowledgeId());
            knowledgeRepository.findById(knowledgeId)
                    .orElseThrow(() -> new KnowledgeNotFoundException(knowledgeId));
            appliedKnowledge.updateKnowledge(knowledgeId);
        }

        if (command.getLevelId() != null) {
            LevelId levelId = LevelId.of(command.getLevelId());
            levelRepository.findById(levelId)
                    .orElseThrow(() -> new LevelNotFoundException(levelId));
            appliedKnowledge.updateLevel(levelId);
        }

        if (command.getStartDate() != null) {
            appliedKnowledge.updateStartDate(command.getStartDate());
        }

        AppliedKnowledge updated = appliedKnowledgeRepository.save(appliedKnowledge);
        return AppliedKnowledgeMapper.toDTO(updated);
    }

    public void deleteAppliedKnowledge(Long id) {
        AppliedKnowledge appliedKnowledge = appliedKnowledgeRepository.findById(AppliedKnowledgeId.of(id))
                .orElseThrow(() -> new AppliedKnowledgeNotFoundException(id));

        if (!appliedKnowledge.canBeDeleted()) {
            throw new IllegalStateException("Applied knowledge with associated knowledge or level cannot be deleted");
        }

        appliedKnowledgeRepository.delete(AppliedKnowledgeId.of(id));
    }
}