package com.CoopCredit.CoopCredit.infrastructure.configuration.exception;


import com.CoopCredit.CoopCredit.domain.exceptions.DomainValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ProblemDetailGlobalExceptionHandler {

    // URI base para el tipo de error (se puede mapear a documentación)
    private static final String PROBLEM_DETAIL_BASE_URI = "/errors/";

    // --------------------------------------------------------------------------
    // 1. Manejo de Excepciones de Dominio (DomainValidationException)
    // --------------------------------------------------------------------------
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ProblemDetail> handleDomainValidationException(
            DomainValidationException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Crear el objeto ProblemDetail
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());

        // RFC 7807: Campos adicionales
        problemDetail.setTitle("Domain Validation Failed");
        problemDetail.setType(URI.create(PROBLEM_DETAIL_BASE_URI + "domain-validation-error"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(status).body(problemDetail);
    }

    // --------------------------------------------------------------------------
    // 2. Manejo de Errores de Validación de Entrada (DTOs con @NotNull, @Valid)
    // --------------------------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Creamos un mapa para incluir todos los errores de campo
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status, "The application has formatting or validation errors in the fields.");

        problemDetail.setTitle("Invalid Input Data");
        problemDetail.setType(URI.create(PROBLEM_DETAIL_BASE_URI + "invalid-input"));
        problemDetail.setProperty("errors", errors); // Agregamos el mapa de errores
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(status).body(problemDetail);
    }

    // --------------------------------------------------------------------------
    // 3. Manejo de Excepciones Genéricas (Catch-all)
    // --------------------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status, "An unexpected error occurred on the server: " + ex.getMessage());

        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create(PROBLEM_DETAIL_BASE_URI + "internal-server-error"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(status).body(problemDetail);
    }
}