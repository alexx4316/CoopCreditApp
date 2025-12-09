-- TABLA 1: USUARIOS (Pilar de Seguridad)
-- Contiene las credenciales y roles para la autenticación
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- TABLA 2: AFILIADOS (Entidad principal)
-- Contiene los datos personales y salariales del solicitante
CREATE TABLE affiliates (
    id BIGSERIAL PRIMARY KEY,
    document_id VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    monthly_salary NUMERIC(10, 2) NOT NULL,
    date_of_birth DATE,
    join_date DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(50) NOT NULL,

    -- Enlace de 1:1 con la tabla users.
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_affiliate_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- TABLA 3: SOLICITUDES DE CRÉDITO
-- Contiene los detalles de la transacción de crédito
CREATE TABLE credit_applications (
    id BIGSERIAL PRIMARY KEY,
    application_code VARCHAR(100) UNIQUE NOT NULL,
    requested_amount NUMERIC(10, 2) NOTNULL,
    requested_terms_in_months INTEGER NOT NULL,
    application_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,            -- PENDING, APPROVED, REJECTED

    -- Enlace Many:1 con la tabla affiliates
    affiliate_id BIGINT NOT NULL,
    CONSTRAINT fk_application_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliates(id)
);