package com.CoopCredit.CoopCredit.infrastructure.adapter.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskResponseDto {

    private Integer creditScore;
    private String riskLevel;
    private String riskDetails;


}