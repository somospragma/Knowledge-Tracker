package com.pragma.vc.tracker.knowledgecatalog.application.usecase;

import com.pragma.vc.tracker.knowledgecatalog.application.dto.CreateLevelCommand;
import com.pragma.vc.tracker.knowledgecatalog.application.dto.LevelDTO;
import com.pragma.vc.tracker.knowledgecatalog.application.mapper.LevelMapper;
import com.pragma.vc.tracker.knowledgecatalog.domain.exception.LevelNotFoundException;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.Level;
import com.pragma.vc.tracker.knowledgecatalog.domain.model.LevelId;
import com.pragma.vc.tracker.knowledgecatalog.domain.repository.LevelRepository;

import java.util.List;
import java.util.stream.Collectors;

public class LevelService {

    private final LevelRepository levelRepository;

    public LevelService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    public LevelDTO createLevel(CreateLevelCommand command) {
        Level level = Level.create(command.getName(), command.getAttributes());
        Level saved = levelRepository.save(level);
        return LevelMapper.toDTO(saved);
    }

    public LevelDTO getLevelById(Long id) {
        LevelId levelId = LevelId.of(id);
        Level level = levelRepository.findById(levelId)
            .orElseThrow(() -> new LevelNotFoundException(levelId));
        return LevelMapper.toDTO(level);
    }

    public List<LevelDTO> getAllLevels() {
        return levelRepository.findAll().stream()
            .map(LevelMapper::toDTO)
            .collect(Collectors.toList());
    }

    public LevelDTO updateLevelName(Long id, String newName) {
        LevelId levelId = LevelId.of(id);
        Level level = levelRepository.findById(levelId)
            .orElseThrow(() -> new LevelNotFoundException(levelId));

        level.updateName(newName);
        Level updated = levelRepository.save(level);
        return LevelMapper.toDTO(updated);
    }

    public void deleteLevel(Long id) {
        LevelId levelId = LevelId.of(id);
        if (!levelRepository.findById(levelId).isPresent()) {
            throw new LevelNotFoundException(levelId);
        }
        levelRepository.deleteById(levelId);
    }
}
