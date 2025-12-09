package com.CoopCredit.CoopCredit.infrastructure.integration;


import com.CoopCredit.CoopCredit.application.rest.auth.dto.LoginRequest;
import com.CoopCredit.CoopCredit.application.rest.creditapplication.dto.CreditApplicationRequest;
import com.CoopCredit.CoopCredit.domain.model.ApplicationStatus;
import com.CoopCredit.CoopCredit.infrastructure.config.TestcontainersConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

// Usamos el contexto completo de Spring Boot
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // Configura MockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Aplica la configuración de Testcontainers para usar PostgreSQL
public class CreditApplicationFlowTest extends TestcontainersConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;
    private static final String TEST_USERNAME = "test.user";
    private static final String TEST_PASSWORD = "Password123";

    // ----------------------------------------------------------------------
    // FASE 1: Configuración (Registro y Login)
    // ----------------------------------------------------------------------
    @BeforeAll
    void setupAuthentication() throws Exception {
        // Ejecución de la migración V3__initial_data.sql con datos del usuario de prueba:
        // Se asume que en V3 tienes un usuario con username 'test.user' y password hasheado
        // y un Afiliado ACTIVO con salario suficiente y antigüedad > 6 meses.

        // 1. LOGIN: Obtener el JWT para las peticiones posteriores
        LoginRequest loginRequest = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Extraer el token del cuerpo de la respuesta
        String responseContent = result.getResponse().getContentAsString();
        jwtToken = objectMapper.readTree(responseContent).get("token").asText();

        System.out.println("JWT Obtenido: " + jwtToken);
        assertNotNull(jwtToken, "El token JWT no debe ser nulo");
    }

    // ----------------------------------------------------------------------
    // FASE 2: Pruebas del Flujo de Solicitud (Llama al Mock de Riesgo)
    // ----------------------------------------------------------------------

    @Test
    void shouldApproveApplication_whenRiskIsLowAndRatioIsCorrect() throws Exception {
        // ARRANGE: Solicitud que debería ser aprobada
        // Asumiendo Salario del Afiliado de $10,000.00 -> Max Cuota es $3,000.00
        CreditApplicationRequest request = new CreditApplicationRequest(
                new BigDecimal("20000.00"), // Monto
                10 // Plazo
        );
        // Cuota mensual calculada: $2,000.00 (OK)

        // ACT & ASSERT
        mockMvc.perform(post("/api/v1/applications")
                        .header("Authorization", "Bearer " + jwtToken) // Seguridad JWT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                // 1. Verificación del Status HTTP y del Flujo
                .andExpect(status().isCreated())

                // 2. Verificación del Resultado de la Evaluación
                .andExpect(jsonPath("$.status", is(ApplicationStatus.APPROVED.name())))
                .andExpect(jsonPath("$.requestedAmount", is(20000.00)))
                .andExpect(jsonPath("$.calculatedMonthlyInstallment", is(2000.00))) // 20000/10 = 2000
                .andExpect(jsonPath("$.applicationCode").exists());
    }

    @Test
    void shouldRejectApplication_whenDebtToIncomeRatioExceeded() throws Exception {
        // ARRANGE: Solicitud que debería ser rechazada por ratio
        // Salario $10,000.00 -> Max Cuota es $3,000.00
        CreditApplicationRequest request = new CreditApplicationRequest(
                new BigDecimal("50000.00"), // Monto
                10 // Plazo
        );
        // Cuota mensual calculada: $5,000.00 (EXCEDE)

        // ACT & ASSERT: Esperamos un 400 Bad Request debido a la DomainValidationException
        mockMvc.perform(post("/api/v1/applications")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                // 1. Verificación del Status HTTP y del ProblemDetail
                .andExpect(status().isBadRequest())

                // 2. Verificación del Cuerpo del Error (ProblemDetail RFC 7807)
                .andExpect(jsonPath("$.title", is("Domain Validation Failed")))
                .andExpect(jsonPath("$.detail").value(
                        org.hamcrest.Matchers.containsString("exceeds 30% of salary")))
                .andExpect(jsonPath("$.status", is(400)));
    }
}