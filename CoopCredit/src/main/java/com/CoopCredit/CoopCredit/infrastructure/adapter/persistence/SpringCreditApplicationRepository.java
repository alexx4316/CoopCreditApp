package com.CoopCredit.CoopCredit.infrastructure.adapter.persistence;


import com.CoopCredit.CoopCredit.domain.model.ApplicationStatus;
import com.CoopCredit.CoopCredit.infrastructure.entity.CreditApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SpringCreditApplicationRepository extends JpaRepository<CreditApplicationEntity, Long> {

    Optional<CreditApplicationEntity> findByApplicationCode(String applicationCode);

    List<CreditApplicationEntity> findByAffiliate_Id(Long affiliateId);

    List<CreditApplicationEntity> findByStatus(ApplicationStatus status);

    @Query("SELECT ca FROM CreditApplicationEntity ca " +
            "JOIN FETCH ca.affiliate a " +
            "WHERE ca.id = :id")
    Optional<CreditApplicationEntity> findByIdWithAffiliate(@Param("id") Long id);
}