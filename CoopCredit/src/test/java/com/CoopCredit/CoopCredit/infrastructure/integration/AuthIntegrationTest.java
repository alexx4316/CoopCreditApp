package com.CoopCredit.CoopCredit.infrastructure.integration;


import com.CoopCredit.CoopCredit.application.rest.auth.dto.LoginRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthIntegrationTest extends TestcontainersConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Se asume la existencia de 'test.user' en la migración V3
    private static final String VALID_USERNAME = "test.user";
    private static final String VALID_PASSWORD = "Password123";
    private String validToken;

    @BeforeAll
    void setup() throws Exception {
        // Obtener un token válido una sola vez
        LoginRequest loginRequest = new LoginRequest(VALID_USERNAME, VALID_PASSWORD);
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        validToken = objectMapper.readTree(responseContent).get("token").asText();
    }

    @Test
    void login_shouldReturnToken_withValidCredentials() throws Exception {
        // La prueba principal se hace en @BeforeAll, esta solo verifica el 200
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(VALID_USERNAME, VALID_PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_shouldFail_withInvalidPassword() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(VALID_USERNAME, "WrongPass"))))
                .andExpect(status().isUnauthorized()); // O 401
    }

    @Test
    void protectedEndpoint_shouldReturn401_withoutToken() throws Exception {
        // Probar un endpoint protegido (e.g., /applications)
        mockMvc.perform(post("/api/v1/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requestedAmount\": 1000, \"requestedTermsInMonths\": 10}"))
                .andExpect(status().isUnauthorized()); // Spring Security devuelve 401
    }

    @Test
    void protectedEndpoint_shouldAllowAccess_withValidToken() throws Exception {
        // El test de flujo de crédito ya verifica esto, pero se añade aquí por completitud.
        // Simplemente validamos que un endpoint protegido no devuelva 401.
        mockMvc.perform(post("/api/v1/applications")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        // Usamos una solicitud INVÁLIDA para que falle por validación de dominio (400), no por seguridad (401)
                        .content("{\"requestedAmount\": 50000000.00, \"requestedTermsInMonths\": 1}"))
                .andExpect(status().isBadRequest()); // Esperamos 400 (error de negocio), no 401 (error de auth)
    }
}