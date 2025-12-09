package com.CoopCredit.CoopCredit.infrastructure.adapter.persistence;


import com.CoopCredit.CoopCredit.domain.model.ApplicationStatus;
import com.CoopCredit.CoopCredit.domain.model.CreditApplication;
import com.CoopCredit.CoopCredit.domain.port.out.CreditApplicationRepositoryPort;
import com.CoopCredit.CoopCredit.infrastructure.adapter.persistence.SpringCreditApplicationRepository;
import com.CoopCredit.CoopCredit.infrastructure.mapper.CreditApplicationMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CreditApplicationJpaAdapter implements CreditApplicationRepositoryPort {

    private final SpringCreditApplicationRepository repository;
    private final CreditApplicationMapper mapper;

    public CreditApplicationJpaAdapter(SpringCreditApplicationRepository repository, CreditApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CreditApplication save(CreditApplication application) {
        return mapper.toDomain(repository.save(mapper.toEntity(application)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreditApplication> findById(Long id) {
        return repository.findByIdWithAffiliate(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreditApplication> findByApplicationCode(String applicationCode) {
        return repository.findByApplicationCode(applicationCode)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditApplication> findAllByAffiliateId(Long affiliateId) {
        return repository.findByAffiliate_Id(affiliateId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditApplication> findAllByStatus(ApplicationStatus status) {
        return repository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}