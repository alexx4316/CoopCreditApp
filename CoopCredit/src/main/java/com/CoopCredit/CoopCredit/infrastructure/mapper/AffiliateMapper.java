package com.CoopCredit.CoopCredit.infrastructure.mapper;

import com.CoopCredit.CoopCredit.domain.model.Affiliate;
import com.CoopCredit.CoopCredit.infrastructure.entity.AffiliateEntity;
import org.springframework.stereotype.Component;

@Component
public class AffiliateMapper {

    public AffiliateEntity toEntity(Affiliate domain) {
        if (domain == null) {
            return null;
        }
        AffiliateEntity entity = new AffiliateEntity();
        entity.setId(domain.getId());
        entity.setDocumentId(domain.getDocumentId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setMonthlySalary(domain.getMonthlySalary());
        entity.setDateOfBirth(domain.getDateOfBirth());
        entity.setJoinDate(domain.getJoinDate());
        entity.setStatus(domain.getStatus());
        entity.setUserId(domain.getUserId());
        return entity;
    }

    public Affiliate toDomain(AffiliateEntity entity) {
        if (entity == null) {
            return null;
        }
        Affiliate domain = new Affiliate();
        domain.setId(entity.getId());
        domain.setDocumentId(entity.getDocumentId());
        domain.setFirstName(entity.getFirstName());
        domain.setLastName(entity.getLastName());
        domain.setEmail(entity.getEmail());
        domain.setPhone(entity.getPhone());
        domain.setMonthlySalary(entity.getMonthlySalary());
        domain.setDateOfBirth(entity.getDateOfBirth());
        domain.setJoinDate(entity.getJoinDate());
        domain.setStatus(entity.getStatus());
        domain.setUserId(entity.getUserId());
        return domain;
    }
}
