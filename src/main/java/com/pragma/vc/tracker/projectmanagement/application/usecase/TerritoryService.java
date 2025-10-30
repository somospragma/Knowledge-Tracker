package com.pragma.vc.tracker.projectmanagement.application.usecase;

import com.pragma.vc.tracker.projectmanagement.application.dto.CreateTerritoryCommand;
import com.pragma.vc.tracker.projectmanagement.application.dto.TerritoryDTO;
import com.pragma.vc.tracker.projectmanagement.application.exception.TerritoryAlreadyExistsException;
import com.pragma.vc.tracker.projectmanagement.application.exception.TerritoryNotFoundException;
import com.pragma.vc.tracker.projectmanagement.application.mapper.TerritoryMapper;
import com.pragma.vc.tracker.projectmanagement.domain.model.Territory;
import com.pragma.vc.tracker.projectmanagement.domain.model.TerritoryId;
import com.pragma.vc.tracker.projectmanagement.domain.repository.TerritoryRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Service for Territory use cases
 * Orchestrates domain logic and coordinates with repositories
 */
public class TerritoryService {

    private final TerritoryRepository territoryRepository;

    public TerritoryService(TerritoryRepository territoryRepository) {
        this.territoryRepository = territoryRepository;
    }

    /**
     * Create a new Territory
     */
    public TerritoryDTO createTerritory(CreateTerritoryCommand command) {
        // Check if territory already exists
        if (territoryRepository.existsByName(command.getName())) {
            throw new TerritoryAlreadyExistsException(command.getName());
        }

        // Create domain entity
        Territory territory = Territory.create(command.getName());

        // Save and return
        Territory savedTerritory = territoryRepository.save(territory);
        return TerritoryMapper.toDTO(savedTerritory);
    }

    /**
     * Get a Territory by ID
     */
    public TerritoryDTO getTerritoryById(Long id) {
        TerritoryId territoryId = TerritoryId.of(id);
        Territory territory = territoryRepository.findById(territoryId)
            .orElseThrow(() -> new TerritoryNotFoundException(territoryId));

        return TerritoryMapper.toDTO(territory);
    }

    /**
     * Get a Territory by name
     */
    public TerritoryDTO getTerritoryByName(String name) {
        Territory territory = territoryRepository.findByName(name)
            .orElseThrow(() -> new TerritoryNotFoundException(name));

        return TerritoryMapper.toDTO(territory);
    }

    /**
     * Get all Territories
     */
    public List<TerritoryDTO> getAllTerritories() {
        return territoryRepository.findAll().stream()
            .map(TerritoryMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Update a Territory's name
     */
    public TerritoryDTO updateTerritory(Long id, String newName) {
        TerritoryId territoryId = TerritoryId.of(id);
        Territory territory = territoryRepository.findById(territoryId)
            .orElseThrow(() -> new TerritoryNotFoundException(territoryId));

        // Check if new name already exists (and it's not the same territory)
        territoryRepository.findByName(newName).ifPresent(existingTerritory -> {
            if (!existingTerritory.getId().equals(territoryId)) {
                throw new TerritoryAlreadyExistsException(newName);
            }
        });

        territory.updateName(newName);
        Territory updatedTerritory = territoryRepository.save(territory);

        return TerritoryMapper.toDTO(updatedTerritory);
    }

    /**
     * Delete a Territory
     */
    public void deleteTerritory(Long id) {
        TerritoryId territoryId = TerritoryId.of(id);
        if (!territoryRepository.existsById(territoryId)) {
            throw new TerritoryNotFoundException(territoryId);
        }

        territoryRepository.deleteById(territoryId);
    }
}
