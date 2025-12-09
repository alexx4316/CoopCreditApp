package com.CoopCredit.CoopCredit.infrastructure.adapter.persistence;


import com.CoopCredit.CoopCredit.domain.model.Affiliate;
import com.CoopCredit.CoopCredit.domain.port.out.AffiliateRepositoryPort;
import com.CoopCredit.CoopCredit.infrastructure.entity.AffiliateEntity;
import com.CoopCredit.CoopCredit.infrastructure.mapper.AffiliateMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Component
public class AffiliateJpaAdapter implements AffiliateRepositoryPort {

    private final SpringAffiliateRepository repository;
    private final AffiliateMapper mapper;

    public AffiliateJpaAdapter(SpringAffiliateRepository repository, AffiliateMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Affiliate save(Affiliate affiliate) {

        AffiliateEntity entity = mapper.toEntity(affiliate);
        AffiliateEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Affiliate> findByDocumentId(String documentId) {
        return repository.findByDocumentId(documentId)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Affiliate> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Affiliate> findByUserUsername(String username) {
        return repository.findByUserUsername(username)
                .map(mapper::toDomain);
    }

}