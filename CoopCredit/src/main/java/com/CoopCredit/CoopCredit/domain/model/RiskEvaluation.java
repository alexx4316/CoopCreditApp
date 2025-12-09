package com.CoopCredit.CoopCredit.domain.model;

import java.time.LocalDateTime;


public class RiskEvaluation {
    private Long id;
    private String documentId;
    private Integer creditScore;
    private RiskLevel riskLevel;
    private String riskDetails;
    private LocalDateTime evaluationTimestamp;
    private CreditApplication application;

    public RiskEvaluation() {
    }

    public RiskEvaluation(Long id, String documentId, Integer creditScore, RiskLevel riskLevel, String riskDetails, LocalDateTime evaluationTimestamp, CreditApplication application) {
        this.id = id;
        this.documentId = documentId;
        this.creditScore = creditScore;
        this.riskLevel = riskLevel;
        this.riskDetails = riskDetails;
        this.evaluationTimestamp = evaluationTimestamp;
        this.application = application;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskDetails() {
        return riskDetails;
    }

    public void setRiskDetails(String riskDetails) {
        this.riskDetails = riskDetails;
    }

    public LocalDateTime getEvaluationTimestamp() {
        return evaluationTimestamp;
    }

    public void setEvaluationTimestamp(LocalDateTime evaluationTimestamp) {
        this.evaluationTimestamp = evaluationTimestamp;
    }

    public CreditApplication getApplication() {
        return application;
    }

    public void setApplication(CreditApplication application) {
        this.application = application;
    }
}