package com.pragma.vc.tracker.knowledgecatalog.application.usecase;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.CreateKnowledgeCommand;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.KnowledgeDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.mapper.KnowledgeMapper;
import com.pragma.vc.tracker.knowledgecatalog.domain.exception.CategoryNotFoundException;
import com.pragma.vc.tracker.knowledgecatalog.domain.exception.KnowledgeNotFoundException;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.*;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.CategoryRepository;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.KnowledgeRepository;

import java.util.List;
import java.util.stream.Collectors;

public class KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;
    private final CategoryRepository categoryRepository;

    public KnowledgeService(KnowledgeRepository knowledgeRepository, CategoryRepository categoryRepository) {
        this.knowledgeRepository = knowledgeRepository;
        this.categoryRepository = categoryRepository;
    }

    public KnowledgeDTO createKnowledge(CreateKnowledgeCommand command) {
        CategoryId categoryId = CategoryId.of(command.getCategoryId());
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        ApprovalStatus status = KnowledgeMapper.parseStatus(command.getApprovalStatus());
        Knowledge knowledge = Knowledge.create(
            categoryId,
            command.getName(),
            command.getDescription(),
            status,
            command.getAttributes()
        );

        Knowledge saved = knowledgeRepository.save(knowledge);
        return KnowledgeMapper.toDTO(saved, category.getName());
    }

    public KnowledgeDTO getKnowledgeById(Long id) {
        KnowledgeId knowledgeId = KnowledgeId.of(id);
        Knowledge knowledge = knowledgeRepository.findById(knowledgeId)
            .orElseThrow(() -> new KnowledgeNotFoundException(knowledgeId));

        Category category = categoryRepository.findById(knowledge.getCategoryId()).orElse(null);
        String categoryName = category != null ? category.getName() : null;

        return KnowledgeMapper.toDTO(knowledge, categoryName);
    }

    public List<KnowledgeDTO> getAllKnowledge() {
        return knowledgeRepository.findAll().stream()
            .map(knowledge -> {
                Category category = categoryRepository.findById(knowledge.getCategoryId()).orElse(null);
                return KnowledgeMapper.toDTO(knowledge, category != null ? category.getName() : null);
            })
            .collect(Collectors.toList());
    }

    public List<KnowledgeDTO> getKnowledgeByCategoryId(Long categoryId) {
        CategoryId catId = CategoryId.of(categoryId);
        Category category = categoryRepository.findById(catId)
            .orElseThrow(() -> new CategoryNotFoundException(catId));

        return knowledgeRepository.findByCategoryId(catId).stream()
            .map(knowledge -> KnowledgeMapper.toDTO(knowledge, category.getName()))
            .collect(Collectors.toList());
    }

    public List<KnowledgeDTO> getKnowledgeByApprovalStatus(String status) {
        ApprovalStatus approvalStatus = KnowledgeMapper.parseStatus(status);
        return knowledgeRepository.findByApprovalStatus(approvalStatus).stream()
            .map(knowledge -> {
                Category category = categoryRepository.findById(knowledge.getCategoryId()).orElse(null);
                return KnowledgeMapper.toDTO(knowledge, category != null ? category.getName() : null);
            })
            .collect(Collectors.toList());
    }

    public List<KnowledgeDTO> searchKnowledgeByName(String name) {
        return knowledgeRepository.searchByName(name).stream()
            .map(knowledge -> {
                Category category = categoryRepository.findById(knowledge.getCategoryId()).orElse(null);
                return KnowledgeMapper.toDTO(knowledge, category != null ? category.getName() : null);
            })
            .collect(Collectors.toList());
    }

    public KnowledgeDTO approveKnowledge(Long id) {
        KnowledgeId knowledgeId = KnowledgeId.of(id);
        Knowledge knowledge = knowledgeRepository.findById(knowledgeId)
            .orElseThrow(() -> new KnowledgeNotFoundException(knowledgeId));

        knowledge.approve();
        Knowledge updated = knowledgeRepository.save(knowledge);

        Category category = categoryRepository.findById(knowledge.getCategoryId()).orElse(null);
        return KnowledgeMapper.toDTO(updated, category != null ? category.getName() : null);
    }

    public KnowledgeDTO rejectKnowledge(Long id) {
        KnowledgeId knowledgeId = KnowledgeId.of(id);
        Knowledge knowledge = knowledgeRepository.findById(knowledgeId)
            .orElseThrow(() -> new KnowledgeNotFoundException(knowledgeId));

        knowledge.reject();
        Knowledge updated = knowledgeRepository.save(knowledge);

        Category category = categoryRepository.findById(knowledge.getCategoryId()).orElse(null);
        return KnowledgeMapper.toDTO(updated, category != null ? category.getName() : null);
    }

    public KnowledgeDTO resubmitKnowledge(Long id) {
        KnowledgeId knowledgeId = KnowledgeId.of(id);
        Knowledge knowledge = knowledgeRepository.findById(knowledgeId)
            .orElseThrow(() -> new KnowledgeNotFoundException(knowledgeId));

        knowledge.resubmit();
        Knowledge updated = knowledgeRepository.save(knowledge);

        Category category = categoryRepository.findById(knowledge.getCategoryId()).orElse(null);
        return KnowledgeMapper.toDTO(updated, category != null ? category.getName() : null);
    }

    public void deleteKnowledge(Long id) {
        KnowledgeId knowledgeId = KnowledgeId.of(id);
        Knowledge knowledge = knowledgeRepository.findById(knowledgeId)
            .orElseThrow(() -> new KnowledgeNotFoundException(knowledgeId));

        if (!knowledge.canBeDeleted()) {
            throw new IllegalStateException("Approved knowledge cannot be deleted");
        }
        knowledgeRepository.deleteById(knowledgeId);
    }
}
