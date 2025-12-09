package com.CoopCredit.CoopCredit.domain.port.out;

import com.CoopCredit.CoopCredit.domain.model.ApplicationStatus;
import com.CoopCredit.CoopCredit.domain.model.CreditApplication;

import java.util.List;
import java.util.Optional;

public interface CreditApplicationRepositoryPort {
    CreditApplication save(CreditApplication application);
    Optional<CreditApplication> findById(Long id);
    Optional<CreditApplication> findByApplicationCode(String applicationCode);
    List<CreditApplication> findAllByAffiliateId(Long affiliateId);
    List<CreditApplication> findAllByStatus(ApplicationStatus status);
}