package com.pragma.vc.tracker.usermanagement.application.mapper;

import com.pragma.vc.tracker.usermanagement.application.dto.UserDTO;
import com.pragma.vc.tracker.usermanagement.domain.model.User;

/**
 * Mapper for converting between User domain model and UserDTO.
 * This is a plain Java class following hexagonal architecture principles.
 */
public class UserMapper {

    /**
     * Converts a User domain model to UserDTO
     *
     * @param user Domain user entity
     * @return UserDTO
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId().getValue().toString());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setSystemRole(user.getSystemRole().name());
        dto.setActive(user.isActive());

        return dto;
    }
}
