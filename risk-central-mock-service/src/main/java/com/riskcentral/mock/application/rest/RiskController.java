package com.riskcentral.mock.application.rest;

import com.riskcentral.mock.application.dto.DocumentRequest;
import com.riskcentral.mock.application.dto.RiskResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Random;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class RiskController {

    private static final Logger LOGGER = Logger.getLogger(RiskController.class.getName());

    @PostMapping("/risk-evaluation")
    public ResponseEntity<RiskResponse> evaluateRisk(@RequestBody DocumentRequest request) {
        String documentId = request.getDocumentId();

        // 1. Generar Score Consistente (Usando Hash como Seed)
        // Esto garantiza que el mismo documento SIEMPRE tenga el mismo resultado.
        long seed = documentId.hashCode();
        Random random = new Random(seed);

        // Score: rango entre 300 y 850
        int minScore = 300;
        int maxScore = 850;
        int creditScore = random.nextInt(maxScore - minScore + 1) + minScore;

        // 2. Determinar Nivel de Riesgo
        String riskLevel;
        String riskDetails;

        if (creditScore >= 750) {
            riskLevel = "LOW";
            riskDetails = "Excelente historial crediticio simulado. Riesgo Bajo.";
        } else if (creditScore >= 600) {
            riskLevel = "MEDIUM";
            riskDetails = "Historial aceptable, riesgo moderado simulado.";
        } else {
            riskLevel = "HIGH";
            riskDetails = "Score bajo, presenta riesgos altos simulados.";
        }

        LOGGER.info(String.format("MOCK - Evaluando doc: %s -> Score: %d, Riesgo: %s", documentId, creditScore, riskLevel));

        // 3. Devolver la respuesta
        RiskResponse response = new RiskResponse(creditScore, riskLevel, riskDetails);
        return ResponseEntity.ok(response);
    }
}