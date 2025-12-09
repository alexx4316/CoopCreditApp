package com.CoopCredit.CoopCredit.infrastructure.mapper;

import com.CoopCredit.CoopCredit.domain.model.CreditApplication;
import com.CoopCredit.CoopCredit.infrastructure.entity.AffiliateEntity;
import com.CoopCredit.CoopCredit.infrastructure.entity.CreditApplicationEntity;
import org.springframework.stereotype.Component;

@Component
public class CreditApplicationMapper {

    private final AffiliateMapper affiliateMapper;

    public CreditApplicationMapper(AffiliateMapper affiliateMapper) {
        this.affiliateMapper = affiliateMapper;
    }

    public CreditApplicationEntity toEntity(CreditApplication domain) {
        if (domain == null) return null;
        CreditApplicationEntity entity = new CreditApplicationEntity();
        entity.setId(domain.getId());
        entity.setRequestedAmount(domain.getRequestedAmount());
        entity.setRequestedTermsInMonths(domain.getRequestedTermsInMonths());
        entity.setCalculatedMonthlyInstallment(domain.getCalculatedMonthlyInstallment());
        entity.setApplicationCode(domain.getApplicationCode());
        entity.setStatus(domain.getStatus());

        if (domain.getAffiliate() != null) {
            AffiliateEntity affiliateEntity = affiliateMapper.toEntity(domain.getAffiliate());
            entity.setAffiliate(affiliateEntity);
        }

        return entity;
    }

    public CreditApplication toDomain(CreditApplicationEntity entity) {
        if (entity == null) return null;

        CreditApplication domain = new CreditApplication();
        domain.setId(entity.getId());
        domain.setRequestedAmount(entity.getRequestedAmount());
        domain.setRequestedTermsInMonths(entity.getRequestedTermsInMonths());
        domain.setCalculatedMonthlyInstallment(entity.getCalculatedMonthlyInstallment());
        domain.setApplicationCode(entity.getApplicationCode());
        domain.setStatus(entity.getStatus());

        if (entity.getAffiliate() != null) {
            domain.setAffiliate(affiliateMapper.toDomain(entity.getAffiliate()));
        }

        return domain;
    }
}
