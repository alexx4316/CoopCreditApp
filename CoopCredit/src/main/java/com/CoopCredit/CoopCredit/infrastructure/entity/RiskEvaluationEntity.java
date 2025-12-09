package com.CoopCredit.CoopCredit.infrastructure.entity;


import com.CoopCredit.CoopCredit.domain.model.RiskLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "risk_evaluations")
@Getter
@Setter
public class RiskEvaluationEntity {

    // Usamos el ID de la CreditApplication como clave primaria (Shared Primary Key)
    @Id
    @Column(name = "application_id")
    private Long id;

    @Column(name = "credit_score", nullable = false)
    private Integer creditScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    // Relación @OneToOne: La clave foránea está aquí, referenciando a CreditApplicationEntity
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "application_id")
    private CreditApplicationEntity application;
}