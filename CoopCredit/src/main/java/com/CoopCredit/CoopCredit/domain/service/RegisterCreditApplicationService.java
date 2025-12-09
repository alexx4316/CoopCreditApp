package com.CoopCredit.CoopCredit.domain.service;

import com.CoopCredit.CoopCredit.domain.exceptions.DomainValidationException;
import com.CoopCredit.CoopCredit.domain.model.Affiliate;
import com.CoopCredit.CoopCredit.domain.model.ApplicationStatus;
import com.CoopCredit.CoopCredit.domain.model.CreditApplication;
import com.CoopCredit.CoopCredit.domain.model.RiskEvaluation;
import com.CoopCredit.CoopCredit.domain.port.in.RegisterCreditApplicationPort;
import com.CoopCredit.CoopCredit.domain.port.out.AffiliateRepositoryPort;
import com.CoopCredit.CoopCredit.domain.port.out.CreditApplicationRepositoryPort;
import com.CoopCredit.CoopCredit.domain.port.out.RiskEvaluationPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@Service
public class RegisterCreditApplicationService implements RegisterCreditApplicationPort {

    private final AffiliateRepositoryPort affiliateRepositoryPort;
    private final CreditApplicationRepositoryPort applicationRepositoryPort;
    private final RiskEvaluationPort riskEvaluationPort;

    // Constantes de Reglas de Negocio
    private static final int MINIMUM_AFFILIATION_MONTHS = 6;
    private static final BigDecimal MAX_DEBT_INCOME_RATIO = new BigDecimal("0.30"); // 30%
    private static final String APPLICATION_CODE_PREFIX = "CC-";


    public RegisterCreditApplicationService(
            AffiliateRepositoryPort affiliateRepositoryPort,
            CreditApplicationRepositoryPort applicationRepositoryPort,
            RiskEvaluationPort riskEvaluationPort) {

        this.affiliateRepositoryPort = affiliateRepositoryPort;
        this.applicationRepositoryPort = applicationRepositoryPort;
        this.riskEvaluationPort = riskEvaluationPort;
    }

    @Override
    @Transactional
    public CreditApplication registerAndEvaluate(CreditApplication newApplication, String username) {

        // 1. Cargar el Afiliado y Validaciones Pre-registro
        Affiliate affiliate = affiliateRepositoryPort.findByUserUsername(username)
                .orElseThrow(() -> new DomainValidationException("Affiliate not found for username: " + username));

        validateAffiliateStatus(affiliate);
        validateAffiliationAntiquity(affiliate);

        // 2. Establecer valores iniciales (POJO Mutables)
        newApplication.setAffiliate(affiliate);
        newApplication.setStatus(ApplicationStatus.PENDING); // Estado inicial
        newApplication.setApplicationCode(generateApplicationCode()); // Generar código único

        // 3. Persistir la solicitud en estado PENDING
        CreditApplication savedApplication = applicationRepositoryPort.save(newApplication);

        // 4. Evaluación de Riesgo (Llamada al servicio externo)
        RiskEvaluation riskEvaluation = riskEvaluationPort.evaluate(affiliate.getDocumentId());

        // Linkear la evaluación con la aplicación
        riskEvaluation.setApplication(savedApplication);

        // 5. Aplicar Reglas de Decisión y Calcular Cuota
        // La mutabilidad del POJO RiskEvaluation es resuelta por la persistencia JPA
        return evaluateAndFinalize(savedApplication, affiliate, riskEvaluation);
    }


    // --- Lógica de Validación y Decisión ---

    private void validateAffiliateStatus(Affiliate affiliate) {
        if (!affiliate.getStatus().name().equals("ACTIVE")) {
            throw new DomainValidationException("Affiliate must be ACTIVE to submit a credit application. Current status: " + affiliate.getStatus().name());
        }
    }

    private void validateAffiliationAntiquity(Affiliate affiliate) {
        Period period = Period.between(affiliate.getJoinDate(), LocalDate.now());
        if (period.getYears() * 12 + period.getMonths() < MINIMUM_AFFILIATION_MONTHS) {
            throw new DomainValidationException("Affiliate must have a minimum antiquity of " + MINIMUM_AFFILIATION_MONTHS + " months.");
        }
    }

    private BigDecimal calculateMonthlyInstallment(BigDecimal requestedAmount, Integer terms) {
        // Fórmula de cuota plana simple (sin intereses para mantener la simpleza inicial)
        // Cuota = Monto / Plazo
        if (terms == null || terms <= 0) {
            throw new DomainValidationException("Terms must be a positive number.");
        }
        return requestedAmount.divide(BigDecimal.valueOf(terms), 2, RoundingMode.HALF_UP);
    }

    private CreditApplication evaluateAndFinalize(CreditApplication application, Affiliate affiliate, RiskEvaluation riskEvaluation) {

        BigDecimal installment = calculateMonthlyInstallment(
                application.getRequestedAmount(),
                application.getRequestedTermsInMonths());

        BigDecimal maxAllowedInstallment = affiliate.getMonthlySalary().multiply(MAX_DEBT_INCOME_RATIO);

        // 1. Regla: Relación Cuota/Ingreso (Debt-to-Income Ratio)
        if (installment.compareTo(maxAllowedInstallment) > 0) {
            application.setStatus(ApplicationStatus.REJECTED);
            application.setCalculatedMonthlyInstallment(installment);
            applicationRepositoryPort.save(application);
            throw new DomainValidationException("Application rejected: Monthly installment exceeds 30% of salary (" + maxAllowedInstallment + ").");
        }

        // 2. Regla: Nivel de Riesgo
        boolean isApproved = switch (riskEvaluation.getRiskLevel()) {
            case LOW -> true;
            case MEDIUM -> riskEvaluation.getCreditScore() >= 650; // Ejemplo: Score mínimo para riesgo medio
            case HIGH -> false;
            default -> false;
        };

        // 3. Decisión Final y Persistencia
        application.setCalculatedMonthlyInstallment(installment);

        if (isApproved) {
            application.setStatus(ApplicationStatus.APPROVED);
        } else {
            application.setStatus(ApplicationStatus.REJECTED);
        }

        // Persistir la aplicación con el resultado final y la evaluación de riesgo
        return applicationRepositoryPort.save(application);
    }

    // Generación de código simple. En producción usaríamos un generador más robusto.
    private String generateApplicationCode() {
        return APPLICATION_CODE_PREFIX + System.currentTimeMillis();
    }
}
