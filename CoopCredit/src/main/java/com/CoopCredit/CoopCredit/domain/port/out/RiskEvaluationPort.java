package com.CoopCredit.CoopCredit.domain.port.out;


import com.CoopCredit.CoopCredit.domain.model.RiskEvaluation;

public interface RiskEvaluationPort {
    RiskEvaluation evaluate(String documentId);
}