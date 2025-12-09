package com.CoopCredit.CoopCredit.domain.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public class CreditApplication {
    private Long id;
    private String applicationCode;
    private BigDecimal requestedAmount;
    private Integer requestedTermsInMonths;
    private BigDecimal calculatedMonthlyInstallment;
    private ApplicationStatus status;
    private LocalDateTime applicationDate;
    private Affiliate affiliate;
    private RiskEvaluation riskEvaluation;

    public CreditApplication() {
    }

    public CreditApplication(Long id, String applicationCode, BigDecimal requestedAmount, Integer requestedTermsInMonths, BigDecimal calculatedMonthlyInstallment, ApplicationStatus status, LocalDateTime applicationDate, Affiliate affiliate, RiskEvaluation riskEvaluation) {
        this.id = id;
        this.applicationCode = applicationCode;
        this.requestedAmount = requestedAmount;
        this.requestedTermsInMonths = requestedTermsInMonths;
        this.calculatedMonthlyInstallment = calculatedMonthlyInstallment;
        this.status = status;
        this.applicationDate = applicationDate;
        this.affiliate = affiliate;
        this.riskEvaluation = riskEvaluation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Integer getRequestedTermsInMonths() {
        return requestedTermsInMonths;
    }

    public void setRequestedTermsInMonths(Integer requestedTermsInMonths) {
        this.requestedTermsInMonths = requestedTermsInMonths;
    }

    public BigDecimal getCalculatedMonthlyInstallment() {
        return calculatedMonthlyInstallment;
    }

    public void setCalculatedMonthlyInstallment(BigDecimal calculatedMonthlyInstallment) {
        this.calculatedMonthlyInstallment = calculatedMonthlyInstallment;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Affiliate getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(Affiliate affiliate) {
        this.affiliate = affiliate;
    }

    public RiskEvaluation getRiskEvaluation() {
        return riskEvaluation;
    }

    public void setRiskEvaluation(RiskEvaluation riskEvaluation) {
        this.riskEvaluation = riskEvaluation;
    }
}