package com.CoopCredit.CoopCredit.application.rest.creditapplication.dto;

import com.CoopCredit.CoopCredit.domain.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditApplicationResponse {
    private String applicationCode;
    private BigDecimal requestedAmount;
    private Integer requestedTermsInMonths;
    private BigDecimal calculatedMonthlyInstallment;
    private ApplicationStatus status; // APPROVED o REJECTED
    private LocalDateTime applicationDate;
}
