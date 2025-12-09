package com.CoopCredit.CoopCredit.infrastructure.integration;


import com.CoopCredit.CoopCredit.application.rest.auth.dto.RegisterRequest;
import com.CoopCredit.CoopCredit.infrastructure.config.TestcontainersConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ErrorHandlingIntegrationTest extends TestcontainersConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnProblemDetail_forMethodArgumentNotValidException() throws Exception {
        // Arrange: Enviar un DTO con campos nulos o inválidos (@NotNull en DTO)
        RegisterRequest invalidRequest = new RegisterRequest(
                null, // username: null
                "Password123",
                "DocTest",
                "FirstName",
                "LastName",
                "test@example.com",
                "1234567890",
                new BigDecimal("5000.00"),
                LocalDate.of(1990, 1, 1)
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))

                // 1. Verificar Status y Tipo RFC 7807
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.type", is("/errors/invalid-input")))
                .andExpect(jsonPath("$.title", is("Datos de Entrada Inválidos")))

                // 2. Verificar el detalle de los errores de campo
                .andExpect(jsonPath("$.errors.username").exists());
    }

}
