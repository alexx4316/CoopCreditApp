package com.CoopCredit.CoopCredit.domain.port.out;


import com.CoopCredit.CoopCredit.domain.model.Affiliate;

import java.util.Optional;


public interface AffiliateRepositoryPort {

    Affiliate save(Affiliate affiliate);
    Optional<Affiliate> findByDocumentId(String documentId);
    Optional<Affiliate> findById(Long id);
    Optional<Affiliate> findByUserUsername(String username);
}