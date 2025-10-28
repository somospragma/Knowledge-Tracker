package com.pragma.vc.tracker.projectmanagement.application.usecase;

import com.pragma.vc.tracker.projectmanagement.application.dto.CreateRegionCommand;
import com.pragma.vc.tracker.projectmanagement.application.dto.RegionDTO;
import com.pragma.vc.tracker.projectmanagement.application.exception.RegionAlreadyExistsException;
import com.pragma.vc.tracker.projectmanagement.application.exception.RegionNotFoundException;
import com.pragma.vc.tracker.projectmanagement.application.mapper.RegionMapper;
import com.pragma.vc.tracker.projectmanagement.domain.model.Region;
import com.pragma.vc.tracker.projectmanagement.domain.model.RegionId;
import com.pragma.vc.tracker.projectmanagement.domain.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application Service for Region use cases
 * Orchestrates domain logic and coordinates with repositories
 */
@Service
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    /**
     * Create a new Region
     */
    public RegionDTO createRegion(CreateRegionCommand command) {
        // Check if region already exists
        if (regionRepository.existsByName(command.getName())) {
            throw new RegionAlreadyExistsException(command.getName());
        }

        // Create domain entity
        Region region = Region.create(command.getName());

        // Save and return
        Region savedRegion = regionRepository.save(region);
        return RegionMapper.toDTO(savedRegion);
    }

    /**
     * Get a Region by ID
     */
    @Transactional(readOnly = true)
    public RegionDTO getRegionById(Long id) {
        RegionId regionId = RegionId.of(id);
        Region region = regionRepository.findById(regionId)
            .orElseThrow(() -> new RegionNotFoundException(regionId));

        return RegionMapper.toDTO(region);
    }

    /**
     * Get a Region by name
     */
    @Transactional(readOnly = true)
    public RegionDTO getRegionByName(String name) {
        Region region = regionRepository.findByName(name)
            .orElseThrow(() -> new RegionNotFoundException(name));

        return RegionMapper.toDTO(region);
    }

    /**
     * Get all Regions
     */
    @Transactional(readOnly = true)
    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAll().stream()
            .map(RegionMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Update a Region's name
     */
    public RegionDTO updateRegion(Long id, String newName) {
        RegionId regionId = RegionId.of(id);
        Region region = regionRepository.findById(regionId)
            .orElseThrow(() -> new RegionNotFoundException(regionId));

        // Check if new name already exists (and it's not the same region)
        regionRepository.findByName(newName).ifPresent(existingRegion -> {
            if (!existingRegion.getId().equals(regionId)) {
                throw new RegionAlreadyExistsException(newName);
            }
        });

        region.updateName(newName);
        Region updatedRegion = regionRepository.save(region);

        return RegionMapper.toDTO(updatedRegion);
    }

    /**
     * Delete a Region
     */
    public void deleteRegion(Long id) {
        RegionId regionId = RegionId.of(id);
        if (!regionRepository.existsById(regionId)) {
            throw new RegionNotFoundException(regionId);
        }

        regionRepository.deleteById(regionId);
    }
}
