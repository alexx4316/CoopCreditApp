-- TABLA 4: EVALUACIÓN DE RIESGO
-- Contiene la respuesta de la central de riesgo (risk-central-mock)
CREATE TABLE risk_evaluations (
    application_id BIGINT PRIMARY KEY,
    document_id VARCHAR(50) NOT NULL,
    credit_score INTEGER NOT NULL,
    risk_level VARCHAR(50) NOT NULL,
    risk_details TEXT,
    evaluation_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- FK a credit_applications
    CONSTRAINT fk_risk_application FOREIGN KEY (application_id) REFERENCES credit_applications(id)
);

-- Modificación en credit_applications (Añadir columna de resultado del cálculo)
-- El resultado de la cuota mensual se calcula DEPUÉS de la solicitud.
ALTER TABLE credit_applications
ADD COLUMN calculated_monthly_installment NUMERIC(10, 2);