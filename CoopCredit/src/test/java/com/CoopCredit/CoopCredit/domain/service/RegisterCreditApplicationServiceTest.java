package com.CoopCredit.CoopCredit.domain.service;

import com.CoopCredit.CoopCredit.domain.model.*;
import com.CoopCredit.CoopCredit.domain.port.out.AffiliateRepositoryPort;
import com.CoopCredit.CoopCredit.domain.port.out.CreditApplicationRepositoryPort;
import com.CoopCredit.CoopCredit.domain.port.out.RiskEvaluationPort;
import com.CoopCredit.CoopCredit.domain.exceptions.DomainValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.CoopCredit.CoopCredit.domain.model.AffiliateStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterCreditApplicationServiceTest {

    @Mock
    private AffiliateRepositoryPort affiliateRepositoryPort;

    @Mock
    private CreditApplicationRepositoryPort applicationRepositoryPort;

    @Mock
    private RiskEvaluationPort riskEvaluationPort;

    @InjectMocks
    private RegisterCreditApplicationService service;

    private Affiliate activeAffiliate;
    private CreditApplication validApplication;

    // El setup se ejecuta antes de cada prueba para tener objetos base
    @BeforeEach
    void setUp() {
        // Afiliado base (ID 1, Salario: 10000.00, Antigüedad: 1 año)
        activeAffiliate = new Affiliate();
        activeAffiliate.setId(1L);
        activeAffiliate.setDocumentId("1001");
        activeAffiliate.setStatus(AffiliateStatus.ACTIVE);
        activeAffiliate.setMonthlySalary(new BigDecimal("10000.00"));
        // Antigüedad: 1 año (suficiente)
        activeAffiliate.setJoinDate(LocalDate.now().minusYears(1));

        // Solicitud válida base: Monto 50000.00 a 10 meses
        validApplication = new CreditApplication();
        validApplication.setRequestedAmount(new BigDecimal("50000.00"));
        validApplication.setRequestedTermsInMonths(10);

        // Cuota mensual = 5000.00
        // Ratio máximo permitido (30% de 10000) = 3000.00
        // ESTA SOLICITUD DEBERÍA FALLAR POR RATIO CUOTA/INGRESO

        // Configuración de Mocks genéricos:
        // Cuando se guarde la aplicación, devolver la misma aplicación (simulando persistencia)
        when(applicationRepositoryPort.save(any(CreditApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    // ----------------------------------------------------------------------
    // PRUEBAS DE VALIDACIÓN DE DOMINIO
    // ----------------------------------------------------------------------

    @Test
    void registerAndEvaluate_shouldRejectIfAntiquityIsInsufficient() {
        // Arrange
        // Cambiar la fecha de ingreso para que sea insuficiente (menos de 6 meses)
        activeAffiliate.setJoinDate(LocalDate.now().minusMonths(3));
        when(affiliateRepositoryPort.findById(1L)).thenReturn(Optional.of(activeAffiliate));

        // Act & Assert
        assertThrows(DomainValidationException.class,
                () -> service.registerAndEvaluate(validApplication, 1L),
                "You must raise an exception for insufficient seniority");

        // Verificar que no se llamó al puerto de riesgo ni se persistió la aplicación final
        verify(riskEvaluationPort, never()).evaluate(anyString());
    }

    @Test
    void registerAndEvaluate_shouldRejectIfDebtToIncomeRatioExceeded() {
        // Arrange
        // Salario 10000.00 -> Max Cuota es 3000.00
        // Solicitud 50000.00 / 10 meses = Cuota 5000.00 (Excede)
        when(affiliateRepositoryPort.findById(1L)).thenReturn(Optional.of(activeAffiliate));

        // Mockear el RiskEvaluationPort para simular que no hay problema con el riesgo
        when(riskEvaluationPort.evaluate(anyString())).thenReturn(createRiskEvaluation(RiskLevel.LOW, 780));

        // Act & Assert
        assertThrows(DomainValidationException.class,
                () -> service.registerAndEvaluate(validApplication, 1L),
                "You must file an exception for exceeding the quota/income ratio");

        // Verificar que la aplicación fue persistida en REJECTED aunque no se capture la excepción aquí
        verify(applicationRepositoryPort, times(2)).save(any(CreditApplication.class));
    }

    // ----------------------------------------------------------------------
    // PRUEBAS DE EVALUACIÓN DE RIESGO
    // ----------------------------------------------------------------------

    @Test
    void registerAndEvaluate_shouldApproveIfRiskIsLowAndRatioIsCorrect() {
        // Arrange
        // Ajustar la solicitud para que la cuota NO exceda el ratio (30% de 10000 = 3000)
        // Monto 20000.00 / 10 meses = Cuota 2000.00 (OK)
        validApplication.setRequestedAmount(new BigDecimal("20000.00"));

        when(affiliateRepositoryPort.findById(1L)).thenReturn(Optional.of(activeAffiliate));
        when(riskEvaluationPort.evaluate(anyString())).thenReturn(createRiskEvaluation(RiskLevel.LOW, 800)); // Bajo riesgo

        // Act
        CreditApplication result = service.registerAndEvaluate(validApplication, 1L);

        // Assert
        assertEquals(ApplicationStatus.APPROVED, result.getStatus(),
                "The application must be APPROVED");
        assertEquals(new BigDecimal("2000.00"), result.getCalculatedMonthlyInstallment(),
                "The monthly fee must be calculated correctly");
        verify(riskEvaluationPort, times(1)).evaluate("1001");
        // Verify que se guardó dos veces: PENDING y luego APPROVED
        verify(applicationRepositoryPort, times(2)).save(any(CreditApplication.class));
    }

    @Test
    void registerAndEvaluate_shouldRejectIfRiskIsHigh() {
        // Arrange
        // Ajustar la solicitud para que el ratio sea correcto (OK)
        validApplication.setRequestedAmount(new BigDecimal("20000.00"));

        when(affiliateRepositoryPort.findById(1L)).thenReturn(Optional.of(activeAffiliate));
        // Alto riesgo
        when(riskEvaluationPort.evaluate(anyString())).thenReturn(createRiskEvaluation(RiskLevel.HIGH, 450));

        // Act
        CreditApplication result = service.registerAndEvaluate(validApplication, 1L);

        // Assert
        assertEquals(ApplicationStatus.REJECTED, result.getStatus(),
                "The application must be REJECTED due to HIGH risk");
        verify(riskEvaluationPort, times(1)).evaluate("1001");
    }

    // Metodo auxiliar para crear objetos de riesgo simulados
    private RiskEvaluation createRiskEvaluation(RiskLevel level, Integer score) {
        RiskEvaluation re = new RiskEvaluation();
        re.setRiskLevel(level);
        re.setCreditScore(score);
        re.setDocumentId("1001");
        return re;
    }
}