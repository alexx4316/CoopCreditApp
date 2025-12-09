package com.CoopCredit.CoopCredit.infrastructure.adapter.rest;


import com.CoopCredit.CoopCredit.domain.model.RiskEvaluation;
import com.CoopCredit.CoopCredit.domain.model.RiskLevel;
import com.CoopCredit.CoopCredit.domain.port.out.RiskEvaluationPort;
import com.CoopCredit.CoopCredit.infrastructure.adapter.rest.dto.RiskRequestDto;
import com.CoopCredit.CoopCredit.infrastructure.adapter.rest.dto.RiskResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RiskEvaluationRestAdapter implements RiskEvaluationPort {

    private final RestTemplate restTemplate;

    @Value("${risk-central.service.url}")
    private String riskCentralUrl;

    private static final String EVALUATION_PATH = "/risk-evaluation";

    public RiskEvaluationRestAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public RiskEvaluation evaluate(String documentId) {

        RiskRequestDto requestDto = new RiskRequestDto(documentId);

        try {
            String fullUrl = riskCentralUrl + EVALUATION_PATH;

            ResponseEntity<RiskResponseDto> responseEntity = restTemplate.postForEntity(
                    fullUrl,
                    requestDto,
                    RiskResponseDto.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                return mapToDomain(responseEntity.getBody(), documentId);
            } else {
                throw new RuntimeException("Error calling the risk service. Code:" + responseEntity.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Communication failure with Risk Central Mock Service:" + e.getMessage(), e);
        }
    }

    private RiskEvaluation mapToDomain(RiskResponseDto dto, String documentId) {
        RiskEvaluation domainModel = new RiskEvaluation();

        domainModel.setDocumentId(documentId);
        domainModel.setCreditScore(dto.getCreditScore());
        domainModel.setRiskLevel(RiskLevel.valueOf(dto.getRiskLevel()));
        domainModel.setRiskDetails(dto.getRiskDetails());

        return domainModel;
    }
}