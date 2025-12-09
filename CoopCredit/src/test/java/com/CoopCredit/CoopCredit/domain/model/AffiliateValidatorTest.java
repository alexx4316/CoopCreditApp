package com.CoopCredit.CoopCredit.domain.model;

import com.CoopCredit.CoopCredit.domain.exceptions.DomainValidationException;
import com.CoopCredit.CoopCredit.domain.service.validation.AffiliateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AffiliateValidatorTest {

    private Affiliate validAffiliate;

    @BeforeEach
    void setUp() {
        // Create a valid affiliate object to be used as a base in tests
        validAffiliate = new Affiliate();
        validAffiliate.setDocumentId("123456789");
        validAffiliate.setFirstName("John");
        validAffiliate.setLastName("Doe");
        validAffiliate.setEmail("john.doe@example.com");
        validAffiliate.setDateOfBirth(LocalDate.now().minusYears(20)); // 20 years old
        validAffiliate.setMonthlySalary(new BigDecimal("3000.00"));
    }

    @Test
    void validateNewAffiliate_shouldPassWithValidData() {
        // Act & Assert: No exception should be thrown for a valid affiliate
        assertDoesNotThrow(() -> AffiliateValidator.validateNewAffiliate(validAffiliate));
    }

    @Test
    void validateNewAffiliate_shouldFailForNullSalary() {
        // Arrange
        validAffiliate.setMonthlySalary(null);

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> AffiliateValidator.validateNewAffiliate(validAffiliate));
        assertEquals("Monthly salary must be a positive value.", exception.getMessage());
    }

    @Test
    void validateNewAffiliate_shouldFailForNegativeSalary() {
        // Arrange
        validAffiliate.setMonthlySalary(new BigDecimal("-100.00"));

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> AffiliateValidator.validateNewAffiliate(validAffiliate));
        assertEquals("Monthly salary must be a positive value.", exception.getMessage());
    }
    
    @Test
    void validateNewAffiliate_shouldFailForZeroSalary() {
        // Arrange
        validAffiliate.setMonthlySalary(BigDecimal.ZERO);

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> AffiliateValidator.validateNewAffiliate(validAffiliate));
        assertEquals("Monthly salary must be a positive value.", exception.getMessage());
    }

    @Test
    void validateNewAffiliate_shouldFailForEmptyDocumentId() {
        // Arrange
        validAffiliate.setDocumentId("");

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> AffiliateValidator.validateNewAffiliate(validAffiliate));
        assertEquals("Document ID cannot be empty.", exception.getMessage());
    }

    @Test
    void validateNewAffiliate_shouldFailForInvalidEmailFormat() {
        // Arrange
        validAffiliate.setEmail("invalid-email");

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> AffiliateValidator.validateNewAffiliate(validAffiliate));
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    void validateNewAffiliate_shouldFailForUnderageAffiliate() {
        // Arrange: Set date of birth to 17 years ago
        validAffiliate.setDateOfBirth(LocalDate.now().minusYears(17));

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> AffiliateValidator.validateNewAffiliate(validAffiliate));
        assertEquals("Affiliate must be at least 18 years old.", exception.getMessage());
    }
    
    @Test
    void validateNewAffiliate_shouldFailForEmptyFirstName() {
        // Arrange
        validAffiliate.setFirstName("");

        // Act & Assert
        DomainValidationException exception = assertThrows(DomainValidationException.class,
                () -> AffiliateValidator.validateNewAffiliate(validAffiliate));
        assertEquals("First Name cannot be empty.", exception.getMessage());
    }
}
