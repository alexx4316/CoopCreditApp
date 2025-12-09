package com.CoopCredit.CoopCredit.infrastructure.mapper;


import com.CoopCredit.CoopCredit.domain.model.User;
import com.CoopCredit.CoopCredit.infrastructure.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User domain) {
        if (domain == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        entity.setPasswordHash(domain.getPasswordHash());
        entity.setDocumentId(domain.getDocumentId());
        entity.setRole(domain.getRole());
        entity.setIsActive(domain.getActive());
        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        User domain = new User();
        domain.setId(entity.getId());
        domain.setUsername(entity.getUsername());
        domain.setPasswordHash(entity.getPasswordHash());
        domain.setDocumentId(entity.getDocumentId());
        domain.setRole(entity.getRole());
        domain.setActive(entity.getIsActive());
        return domain;
    }
}