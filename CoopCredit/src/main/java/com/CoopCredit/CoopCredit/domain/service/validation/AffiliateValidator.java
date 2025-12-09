package com.CoopCredit.CoopCredit.domain.service.validation;


import com.CoopCredit.CoopCredit.domain.exceptions.DomainValidationException;
import com.CoopCredit.CoopCredit.domain.model.Affiliate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class AffiliateValidator {

    // RegEx simple para validación de email (puede ser más complejo si es necesario)
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    // Regla de negocio: La cooperativa solo acepta afiliados mayores de 18 años.
    private static final int MINIMUM_AGE_YEARS = 18;

    private AffiliateValidator() {
        // Clase estática de utilidad
    }

    public static void validateNewAffiliate(Affiliate affiliate) {

        // 1. Validación de Salario y Montos
        validateMonthlySalary(affiliate.getMonthlySalary());

        // 2. Validación de Campos de Identificación
        validateDocumentId(affiliate.getDocumentId());

        // 3. Validación de Email
        validateEmailFormat(affiliate.getEmail());

        // 4. Validación de Edad Mínima (para solicitante)
        validateMinimumAge(affiliate.getDateOfBirth());

        // 5. Validación de Nombres y Apellidos
        validateNames(affiliate.getFirstName(), "First Name");
        validateNames(affiliate.getLastName(), "Last Name");
    }

    // --- Métodos de Validación Detallados ---

    private static void validateMonthlySalary(BigDecimal salary) {
        if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("Monthly salary must be a positive value.");
        }
    }

    private static void validateDocumentId(String documentId) {
        if (documentId == null || documentId.isBlank()) {
            throw new DomainValidationException("Document ID cannot be empty.");
        }
        if (documentId.length() < 5 || documentId.length() > 20) {
            throw new DomainValidationException("Document ID length must be between 5 and 20 characters.");
        }
    }

    private static void validateEmailFormat(String email) {
        if (email == null || email.isBlank()) {
            throw new DomainValidationException("Email cannot be empty.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new DomainValidationException("Invalid email format.");
        }
    }

    private static void validateMinimumAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new DomainValidationException("Date of birth is required.");
        }
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        if (age < MINIMUM_AGE_YEARS) {
            throw new DomainValidationException("Affiliate must be at least " + MINIMUM_AGE_YEARS + " years old.");
        }
    }

    private static void validateNames(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException(fieldName + " cannot be empty.");
        }
        if (name.length() < 2 || name.length() > 50) {
            throw new DomainValidationException(fieldName + " length must be between 2 and 50 characters.");
        }
    }
}