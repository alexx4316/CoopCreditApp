package com.CoopCredit.CoopCredit.infrastructure.adapter.persistence;

import com.CoopCredit.CoopCredit.infrastructure.entity.AffiliateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringAffiliateRepository extends JpaRepository<AffiliateEntity, Long> {

    Optional<AffiliateEntity> findByDocumentId(String documentId);
    Optional<AffiliateEntity> findByUserUsername(String username);
}
