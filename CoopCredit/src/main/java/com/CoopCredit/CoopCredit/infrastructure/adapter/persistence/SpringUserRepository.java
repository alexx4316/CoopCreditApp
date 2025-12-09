package com.CoopCredit.CoopCredit.infrastructure.adapter.persistence;


import com.CoopCredit.CoopCredit.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByDocumentId(String documentId);
}