package com.CoopCredit.CoopCredit.application.rest.creditapplication.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditApplicationRequest {

    @NotNull
    @Positive
    private BigDecimal requestedAmount;

    @NotNull
    @Positive
    private Integer requestedTermsInMonths;

}