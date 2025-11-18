package com.pragma.vc.tracker.usermanagement.infrastructure.persistence.mapper;

import com.pragma.vc.tracker.usermanagement.domain.model.SystemRole;
import com.pragma.vc.tracker.usermanagement.domain.model.User;
import com.pragma.vc.tracker.usermanagement.domain.model.UserId;
import com.pragma.vc.tracker.usermanagement.infrastructure.persistence.entity.JpaUserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between domain User and JPA JpaUserEntity
 * Part of the infrastructure layer
 */
@Component
public class UserEntityMapper {

    /**
     * Convert domain User to JPA entity
     */
    public JpaUserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        JpaUserEntity entity = new JpaUserEntity();

        if (user.getId() != null) {
            entity.setId(user.getId().getValue());
        }

        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setSystemRole(toEntityRole(user.getSystemRole()));
        entity.setActive(user.isActive());

        return entity;
    }

    /**
     * Convert JPA entity to domain User
     */
    public User toDomain(JpaUserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.reconstitute(
            UserId.of(entity.getId()),
            entity.getEmail(),
            entity.getFirstName(),
            entity.getLastName(),
            toDomainRole(entity.getSystemRole()),
            entity.isActive()
        );
    }

    /**
     * Convert domain SystemRole to JPA enum
     */
    private JpaUserEntity.SystemRoleEnum toEntityRole(SystemRole role) {
        if (role == null) {
            return null;
        }

        return switch (role) {
            case FULL_ADMINISTRATOR -> JpaUserEntity.SystemRoleEnum.FULL_ADMINISTRATOR;
            case KNOWLEDGE_MANAGER -> JpaUserEntity.SystemRoleEnum.KNOWLEDGE_MANAGER;
            case PROJECT_ACCOUNT_MANAGER -> JpaUserEntity.SystemRoleEnum.PROJECT_ACCOUNT_MANAGER;
            case PEOPLE_MANAGER -> JpaUserEntity.SystemRoleEnum.PEOPLE_MANAGER;
            case USER -> JpaUserEntity.SystemRoleEnum.USER;
        };
    }

    /**
     * Convert JPA enum to domain SystemRole
     */
    private SystemRole toDomainRole(JpaUserEntity.SystemRoleEnum roleEnum) {
        if (roleEnum == null) {
            return null;
        }

        return switch (roleEnum) {
            case FULL_ADMINISTRATOR -> SystemRole.FULL_ADMINISTRATOR;
            case KNOWLEDGE_MANAGER -> SystemRole.KNOWLEDGE_MANAGER;
            case PROJECT_ACCOUNT_MANAGER -> SystemRole.PROJECT_ACCOUNT_MANAGER;
            case PEOPLE_MANAGER -> SystemRole.PEOPLE_MANAGER;
            case USER -> SystemRole.USER;
        };
    }
}
