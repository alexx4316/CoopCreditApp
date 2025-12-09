package com.CoopCredit.CoopCredit.infrastructure.entity;


import com.CoopCredit.CoopCredit.domain.model.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_applications")
@Getter
@Setter
public class CreditApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_code", unique = true, nullable = false)
    private String applicationCode;

    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    // Relación @ManyToOne: Muchas solicitudes pertenecen a un Afiliado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_id", nullable = false)
    private AffiliateEntity affiliate;

    // Relación @OneToOne: Una solicitud tiene una (y solo una) Evaluación de Riesgo
    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private RiskEvaluationEntity riskEvaluation;


}