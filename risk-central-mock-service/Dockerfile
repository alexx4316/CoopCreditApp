# -----------------------------------------------------------------------------
# FASE 1: BUILD (Compilación y Empaquetado)
# -----------------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-focal AS builder

WORKDIR /app

# Copiar archivos de configuración y descargar dependencias
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline

# Copiar el código fuente y compilar
COPY src/ ./src/
RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests

ARG JAR_FILE=target/*.jar

# -----------------------------------------------------------------------------
# FASE 2: RUN (Entorno de Ejecución Ligero)
# -----------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-focal

ARG JAR_FILE

# Copiar el JAR compilado del Mock
COPY --from=builder /app/${JAR_FILE} app.jar

# Exponer el puerto del Mock (8081)
EXPOSE 8081

# Comando para ejecutar el Mock
ENTRYPOINT ["java", "-jar", "/app.jar"]