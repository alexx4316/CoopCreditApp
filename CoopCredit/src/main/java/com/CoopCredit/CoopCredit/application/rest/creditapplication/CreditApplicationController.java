package com.CoopCredit.CoopCredit.application.rest.creditapplication;

import com.CoopCredit.CoopCredit.application.rest.creditapplication.dto.CreditApplicationRequest;
import com.CoopCredit.CoopCredit.application.rest.creditapplication.dto.CreditApplicationResponse;
import com.CoopCredit.CoopCredit.domain.model.CreditApplication;
import com.CoopCredit.CoopCredit.domain.port.in.RegisterCreditApplicationPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
public class CreditApplicationController {

    private final RegisterCreditApplicationPort registerCreditApplicationPort;

    public CreditApplicationController(RegisterCreditApplicationPort registerCreditApplicationPort) {
        this.registerCreditApplicationPort = registerCreditApplicationPort;
    }

    @PostMapping
    public ResponseEntity<CreditApplicationResponse> registerApplication(
            @Valid @RequestBody CreditApplicationRequest request) {

        // 1. Obtener el nombre de usuario del afiliado autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        // 2. Mapear DTO de Entrada a Modelo de Dominio
        CreditApplication domainApplication = mapToDomain(request);

        // 3. Invocar el Caso de Uso
        CreditApplication evaluatedApplication = registerCreditApplicationPort.registerAndEvaluate(
                domainApplication, username);

        // 4. Mapear Modelo de Dominio a DTO de Respuesta
        CreditApplicationResponse response = mapToResponse(evaluatedApplication);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private CreditApplication mapToDomain(CreditApplicationRequest request) {
        CreditApplication application = new CreditApplication();
        application.setRequestedAmount(request.getRequestedAmount());
        application.setRequestedTermsInMonths(request.getRequestedTermsInMonths());
        return application;
    }

    private CreditApplicationResponse mapToResponse(CreditApplication application) {
        CreditApplicationResponse response = new CreditApplicationResponse();
        response.setApplicationCode(application.getApplicationCode());
        response.setRequestedAmount(application.getRequestedAmount());
        response.setRequestedTermsInMonths(application.getRequestedTermsInMonths());
        response.setCalculatedMonthlyInstallment(application.getCalculatedMonthlyInstallment());
        response.setStatus(application.getStatus());
        return response;
    }
}
