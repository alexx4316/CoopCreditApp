-- INDICES: Mejoran el rendimiento de las búsquedas frecuentes
-- Búsqueda de solicitudes por estado (para ROLE_ANALISTA)
CREATE INDEX idx_application_status ON credit_applications (status);

-- Búsqueda de solicitudes por afiliado (para ROLE_AFILIADO)
CREATE INDEX idx_application_affiliate ON credit_applications (affiliate_id);

-- DATOS INICIALES (Usuario Admin por defecto)
-- La contraseña debe estar HASHED antes de la inserción, por seguridad
INSERT INTO users (username, password_hash, role, is_active)
VALUES ('admin', '$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 'ROLE_ADMIN', TRUE);

-- DATOS INICIALES (Usuario de prueba Afiliado)
INSERT INTO users (username, password_hash, role, is_active)
VALUES ('afiliado.test', '$2a$10$YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY', 'ROLE_AFILIADO', TRUE);