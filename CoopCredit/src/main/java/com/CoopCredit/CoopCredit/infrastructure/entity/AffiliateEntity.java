package com.CoopCredit.CoopCredit.infrastructure.entity;

import com.CoopCredit.CoopCredit.domain.model.AffiliateStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "affiliates")
@Getter
@Setter
public class AffiliateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_id", unique = true, nullable = false)
    private String documentId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    // ... otros campos como lastName, email, phone ...

    @Column(name = "monthly_salary", nullable = false)
    private BigDecimal monthlySalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AffiliateStatus status;

    // Relación @OneToMany: Un afiliado puede tener muchas solicitudes
    @OneToMany(mappedBy = "affiliate", fetch = FetchType.LAZY)
    private List<CreditApplicationEntity> applications;
}