# -----------------------------------------------------------------------------
# FASE 1: BUILD (Compilación y Empaquetado)
# -----------------------------------------------------------------------------
# Usamos una imagen base con JDK para compilar el código.
FROM eclipse-temurin:21-jdk-focal AS builder

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar los archivos de configuración de Maven (pom.xml) para aprovechar el cache
COPY pom.xml .

# Descargar las dependencias
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline

# Copiar el código fuente
COPY src/ ./src/

# Empaquetar la aplicación en un JAR ejecutable
RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests

# El JAR generado se encuentra en target/*.jar
# Usaremos 'ls target' para encontrar el nombre exacto, pero asumimos el nombre estándar.
ARG JAR_FILE=target/*.jar

# -----------------------------------------------------------------------------
# FASE 2: RUN (Entorno de Ejecución Ligero)
# -----------------------------------------------------------------------------
# Usamos una imagen JRE (Java Runtime Environment) más pequeña y segura.
FROM eclipse-temurin:21-jre-focal

# Argumento para el JAR file que se compilará
ARG JAR_FILE

# Copiar el JAR compilado de la fase anterior al contenedor final
COPY --from=builder /app/${JAR_FILE} app.jar

# Exponer el puerto de la aplicación (8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]