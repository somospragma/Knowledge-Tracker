package com.pragma.vc.tracker.peoplemanagement.application.usecase;

import com.pragma.vc.tracker.peoplemanagement.application.dto.ChapterDTO;
import com.pragma.vc.tracker.peoplemanagement.application.dto.CreateChapterCommand;
import com.pragma.vc.tracker.peoplemanagement.application.mapper.ChapterMapper;
import com.pragma.vc.tracker.peoplemanagement.domain.exception.ChapterNotFoundException;
import com.pragma.vc.tracker.peoplemanagement.domain.exception.DuplicateChapterNameException;
import com.pragma.vc.tracker.peoplemanagement.domain.model.Chapter;
import com.pragma.vc.tracker.peoplemanagement.domain.model.ChapterId;
import com.pragma.vc.tracker.peoplemanagement.domain.repository.ChapterRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Service for Chapter use cases
 * Orchestrates domain operations and coordinates with repositories
 */
public class ChapterService {

    private final ChapterRepository chapterRepository;

    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    /**
     * Create a new Chapter
     */
    public ChapterDTO createChapter(CreateChapterCommand command) {
        // Check for duplicate name
        if (chapterRepository.existsByName(command.getName())) {
            throw new DuplicateChapterNameException(command.getName());
        }

        // Create domain entity
        Chapter chapter = Chapter.create(command.getName());

        // Save and return
        Chapter savedChapter = chapterRepository.save(chapter);
        return ChapterMapper.toDTO(savedChapter);
    }

    /**
     * Get a Chapter by ID
     */
    public ChapterDTO getChapterById(Long id) {
        ChapterId chapterId = ChapterId.of(id);
        Chapter chapter = chapterRepository.findById(chapterId)
            .orElseThrow(() -> new ChapterNotFoundException(chapterId));
        return ChapterMapper.toDTO(chapter);
    }

    /**
     * Get all Chapters
     */
    public List<ChapterDTO> getAllChapters() {
        return chapterRepository.findAll().stream()
            .map(ChapterMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Update Chapter name
     */
    public ChapterDTO updateChapterName(Long id, String newName) {
        ChapterId chapterId = ChapterId.of(id);
        Chapter chapter = chapterRepository.findById(chapterId)
            .orElseThrow(() -> new ChapterNotFoundException(chapterId));

        // Check if new name is already taken by another chapter
        chapterRepository.findByName(newName).ifPresent(existing -> {
            if (!existing.getId().equals(chapterId)) {
                throw new DuplicateChapterNameException(newName);
            }
        });

        chapter.updateName(newName);
        Chapter updatedChapter = chapterRepository.save(chapter);
        return ChapterMapper.toDTO(updatedChapter);
    }

    /**
     * Delete a Chapter
     */
    public void deleteChapter(Long id) {
        ChapterId chapterId = ChapterId.of(id);
        if (!chapterRepository.findById(chapterId).isPresent()) {
            throw new ChapterNotFoundException(chapterId);
        }
        chapterRepository.deleteById(chapterId);
    }
}