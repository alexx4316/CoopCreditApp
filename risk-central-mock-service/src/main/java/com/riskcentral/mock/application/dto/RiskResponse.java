package com.riskcentral.mock.application.dto;

public class RiskResponse {
    private Integer creditScore;
    private String riskLevel; // LOW, MEDIUM, HIGH
    private String riskDetails;

    public RiskResponse() {}

    public RiskResponse(Integer creditScore, String riskLevel, String riskDetails) {
        this.creditScore = creditScore;
        this.riskLevel = riskLevel;
        this.riskDetails = riskDetails;
    }

    // Getters y Setters
    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRiskDetails() { return riskDetails; }
}