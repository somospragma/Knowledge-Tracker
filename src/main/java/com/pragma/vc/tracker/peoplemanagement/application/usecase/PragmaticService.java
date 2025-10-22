package com.pragma.vc.tracker.peoplemanagement.application.usecase;

import com.pragma.vc.tracker.peoplemanagement.application.dto.CreatePragmaticCommand;
import com.pragma.vc.tracker.peoplemanagement.application.dto.PragmaticDTO;
import com.pragma.vc.tracker.peoplemanagement.application.mapper.PragmaticMapper;
import com.pragma.vc.tracker.peoplemanagement.domain.exception.ChapterNotFoundException;
import com.pragma.vc.tracker.peoplemanagement.domain.exception.DuplicateEmailException;
import com.pragma.vc.tracker.peoplemanagement.domain.exception.PragmaticNotFoundException;
import com.pragma.vc.tracker.peoplemanagement.domain.model.*;
import com.pragma.vc.tracker.peoplemanagement.domain.repository.ChapterRepository;
import com.pragma.vc.tracker.peoplemanagement.domain.repository.PragmaticRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Service for Pragmatic use cases
 * Orchestrates domain operations and coordinates with repositories
 */
public class PragmaticService {

    private final PragmaticRepository pragmaticRepository;
    private final ChapterRepository chapterRepository;

    public PragmaticService(PragmaticRepository pragmaticRepository, ChapterRepository chapterRepository) {
        this.pragmaticRepository = pragmaticRepository;
        this.chapterRepository = chapterRepository;
    }

    /**
     * Create a new Pragmatic
     */
    public PragmaticDTO createPragmatic(CreatePragmaticCommand command) {
        // Parse and validate email
        Email email = Email.of(command.getEmail());

        // Check for duplicate email
        if (pragmaticRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }

        // Verify chapter exists if provided
        final ChapterId chapterId;
        final Chapter chapter;
        if (command.getChapterId() != null) {
            ChapterId chapId = ChapterId.of(command.getChapterId());
            chapter = chapterRepository.findById(chapId)
                .orElseThrow(() -> new ChapterNotFoundException(chapId));
            chapterId = chapId;
        } else {
            chapterId = null;
            chapter = null;
        }

        // Create domain entity
        PragmaticStatus status = PragmaticMapper.parseStatus(command.getStatus());
        Pragmatic pragmatic = Pragmatic.create(
            chapterId,
            email,
            command.getFirstName(),
            command.getLastName(),
            status
        );

        // Save and return
        Pragmatic savedPragmatic = pragmaticRepository.save(pragmatic);
        return PragmaticMapper.toDTO(savedPragmatic, chapter != null ? chapter.getName() : null);
    }

    /**
     * Get a Pragmatic by ID
     */
    public PragmaticDTO getPragmaticById(Long id) {
        PragmaticId pragmaticId = PragmaticId.of(id);
        Pragmatic pragmatic = pragmaticRepository.findById(pragmaticId)
            .orElseThrow(() -> new PragmaticNotFoundException(pragmaticId));

        // Fetch chapter name for DTO
        Chapter chapter = pragmatic.getChapterId() != null
            ? chapterRepository.findById(pragmatic.getChapterId()).orElse(null)
            : null;
        String chapterName = chapter != null ? chapter.getName() : null;

        return PragmaticMapper.toDTO(pragmatic, chapterName);
    }

    /**
     * Get all Pragmatics
     */
    public List<PragmaticDTO> getAllPragmatics() {
        return pragmaticRepository.findAll().stream()
            .map(pragmatic -> {
                Chapter chapter = pragmatic.getChapterId() != null
                    ? chapterRepository.findById(pragmatic.getChapterId()).orElse(null)
                    : null;
                String chapterName = chapter != null ? chapter.getName() : null;
                return PragmaticMapper.toDTO(pragmatic, chapterName);
            })
            .collect(Collectors.toList());
    }

    /**
     * Get Pragmatics by Chapter
     */
    public List<PragmaticDTO> getPragmaticsByChapterId(Long chapterId) {
        ChapterId chapId = ChapterId.of(chapterId);
        Chapter chapter = chapterRepository.findById(chapId)
            .orElseThrow(() -> new ChapterNotFoundException(chapId));

        return pragmaticRepository.findByChapterId(chapId).stream()
            .map(pragmatic -> PragmaticMapper.toDTO(pragmatic, chapter.getName()))
            .collect(Collectors.toList());
    }

    /**
     * Get Pragmatics by status
     */
    public List<PragmaticDTO> getPragmaticsByStatus(String status) {
        PragmaticStatus pragmaticStatus = PragmaticMapper.parseStatus(status);
        return pragmaticRepository.findByStatus(pragmaticStatus).stream()
            .map(pragmatic -> {
                Chapter chapter = pragmatic.getChapterId() != null
                    ? chapterRepository.findById(pragmatic.getChapterId()).orElse(null)
                    : null;
                String chapterName = chapter != null ? chapter.getName() : null;
                return PragmaticMapper.toDTO(pragmatic, chapterName);
            })
            .collect(Collectors.toList());
    }

    /**
     * Update Pragmatic email
     */
    public PragmaticDTO updatePragmaticEmail(Long id, String newEmail) {
        PragmaticId pragmaticId = PragmaticId.of(id);
        Pragmatic pragmatic = pragmaticRepository.findById(pragmaticId)
            .orElseThrow(() -> new PragmaticNotFoundException(pragmaticId));

        Email email = Email.of(newEmail);

        // Check if new email is already taken by another pragmatic
        pragmaticRepository.findByEmail(email).ifPresent(existing -> {
            if (!existing.getId().equals(pragmaticId)) {
                throw new DuplicateEmailException(email);
            }
        });

        pragmatic.updateEmail(email);
        Pragmatic updatedPragmatic = pragmaticRepository.save(pragmatic);

        Chapter chapter = pragmatic.getChapterId() != null
            ? chapterRepository.findById(pragmatic.getChapterId()).orElse(null)
            : null;
        String chapterName = chapter != null ? chapter.getName() : null;

        return PragmaticMapper.toDTO(updatedPragmatic, chapterName);
    }

    /**
     * Assign Pragmatic to Chapter
     */
    public PragmaticDTO assignToChapter(Long pragmaticId, Long chapterId) {
        PragmaticId pragId = PragmaticId.of(pragmaticId);
        Pragmatic pragmatic = pragmaticRepository.findById(pragId)
            .orElseThrow(() -> new PragmaticNotFoundException(pragId));

        ChapterId chapId = ChapterId.of(chapterId);
        Chapter chapter = chapterRepository.findById(chapId)
            .orElseThrow(() -> new ChapterNotFoundException(chapId));

        pragmatic.assignToChapter(chapId);
        Pragmatic updatedPragmatic = pragmaticRepository.save(pragmatic);

        return PragmaticMapper.toDTO(updatedPragmatic, chapter.getName());
    }

    /**
     * Activate a Pragmatic
     */
    public PragmaticDTO activatePragmatic(Long id) {
        PragmaticId pragmaticId = PragmaticId.of(id);
        Pragmatic pragmatic = pragmaticRepository.findById(pragmaticId)
            .orElseThrow(() -> new PragmaticNotFoundException(pragmaticId));

        pragmatic.activate();
        Pragmatic updatedPragmatic = pragmaticRepository.save(pragmatic);

        Chapter chapter = pragmatic.getChapterId() != null
            ? chapterRepository.findById(pragmatic.getChapterId()).orElse(null)
            : null;
        String chapterName = chapter != null ? chapter.getName() : null;

        return PragmaticMapper.toDTO(updatedPragmatic, chapterName);
    }

    /**
     * Deactivate a Pragmatic
     */
    public PragmaticDTO deactivatePragmatic(Long id) {
        PragmaticId pragmaticId = PragmaticId.of(id);
        Pragmatic pragmatic = pragmaticRepository.findById(pragmaticId)
            .orElseThrow(() -> new PragmaticNotFoundException(pragmaticId));

        pragmatic.deactivate();
        Pragmatic updatedPragmatic = pragmaticRepository.save(pragmatic);

        Chapter chapter = pragmatic.getChapterId() != null
            ? chapterRepository.findById(pragmatic.getChapterId()).orElse(null)
            : null;
        String chapterName = chapter != null ? chapter.getName() : null;

        return PragmaticMapper.toDTO(updatedPragmatic, chapterName);
    }

    /**
     * Put Pragmatic on leave
     */
    public PragmaticDTO putPragmaticOnLeave(Long id) {
        PragmaticId pragmaticId = PragmaticId.of(id);
        Pragmatic pragmatic = pragmaticRepository.findById(pragmaticId)
            .orElseThrow(() -> new PragmaticNotFoundException(pragmaticId));

        pragmatic.putOnLeave();
        Pragmatic updatedPragmatic = pragmaticRepository.save(pragmatic);

        Chapter chapter = pragmatic.getChapterId() != null
            ? chapterRepository.findById(pragmatic.getChapterId()).orElse(null)
            : null;
        String chapterName = chapter != null ? chapter.getName() : null;

        return PragmaticMapper.toDTO(updatedPragmatic, chapterName);
    }

    /**
     * Delete a Pragmatic
     */
    public void deletePragmatic(Long id) {
        PragmaticId pragmaticId = PragmaticId.of(id);
        if (!pragmaticRepository.findById(pragmaticId).isPresent()) {
            throw new PragmaticNotFoundException(pragmaticId);
        }
        pragmaticRepository.deleteById(pragmaticId);
    }
}