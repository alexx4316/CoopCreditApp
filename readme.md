# CoopCredit - Sistema de Solicitud y Evaluación de Créditos

Este proyecto es una implementación de un sistema de microservicios para **CoopCredit**, una cooperativa de ahorro y crédito. El sistema digitaliza y automatiza el proceso de solicitud y evaluación de créditos, siguiendo las mejores prácticas de la industria como la Arquitectura Hexagonal, la seguridad con JWT y la contenerización con Docker.

## Tabla de Contenidos
1.  [Arquitectura](#arquitectura)
2.  [Tecnologías Utilizadas](#tecnologías-utilizadas)
3.  [Estructura del Proyecto](#estructura-del-proyecto)
4.  [Prerrequisitos](#prerrequisitos)
5.  [Instalación y Configuración](#instalación-y-configuración)
6.  [Cómo Ejecutar la Aplicación](#cómo-ejecutar-la-aplicación)
7.  [Cómo Probar la Aplicación](#cómo-probar-la-aplicación)
8.  [Endpoints Principales de la API](#endpoints-principales-de-la-api)
9.  [Cumplimiento de Criterios de Aceptación](#cumplimiento-de-criterios-de-aceptación)

---

## Arquitectura

El proyecto está diseñado siguiendo los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)**. Esta arquitectura promueve un bajo acoplamiento y una alta cohesión, aislando la lógica de negocio del dominio de las tecnologías de infraestructura.

-   **Domain (Dominio)**: Contiene la lógica de negocio pura, los modelos (POJOs) y las reglas de validación. No tiene dependencias externas a frameworks.
-   **Application (Aplicación)**: Orquesta los flujos de negocio (casos de uso) y define los puertos de entrada (`input ports`).
-   **Infrastructure (Infraestructura)**: Implementa los puertos de salida (`output ports`) y contiene los detalles tecnológicos como los controladores REST, la configuración de seguridad, los adaptadores de persistencia (JPA) y los clientes para servicios externos.

---

## Tecnologías Utilizadas

-   **Lenguaje**: Java 21
-   **Framework**: Spring Boot 3
-   **Base de Datos**: PostgreSQL
-   **Gestión de Migraciones**: Flyway
-   **Seguridad**: Spring Security con JWT (JSON Web Tokens)
-   **Contenerización**: Docker y Docker Compose
-   **Gestión de Dependencias**: Maven
-   **Pruebas**:
    -   JUnit 5
    -   MockMvc para pruebas de integración de API.
    -   Testcontainers para pruebas de persistencia con una base de datos real.
-   **Manejo de Errores**: ProblemDetail (RFC 7807)
-   **Observabilidad**: Spring Boot Actuator y Micrometer (con endpoint para Prometheus)

---

## Estructura del Proyecto

El repositorio `CoopCreditApp` contiene dos microservicios principales:

1.  `CoopCredit`: El servicio principal que gestiona afiliados y solicitudes de crédito.
2.  `risk-central-mock-service`: Un mock que simula un servicio externo de evaluación de riesgo.

```
CoopCreditApp/
├── CoopCredit/                 # Microservicio principal
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/CoopCredit/CoopCredit/
│   │   │   │   ├── application/  # Controladores REST y DTOs
│   │   │   │   ├── domain/       # Modelos, Puertos y Servicios de Dominio
│   │   │   │   └── infrastructure/ # Adaptadores, Entidades JPA, Mappers, Seguridad
│   │   │   └── resources/
│   │   │       ├── db/migration/ # Scripts de Flyway (V1, V2, V3)
│   │   │       └── application.yml
│   │   └── test/                 # Pruebas unitarias y de integración
│   ├── Dockerfile                # Dockerfile multi-etapa
│   └── pom.xml
├── risk-central-mock-service/  # Mock del servicio de riesgo
└── docker-compose.yml          # Orquesta el despliegue de todos los servicios
```

---

## Prerrequisitos

Para poder ejecutar este proyecto, necesitas tener instalado lo siguiente en tu sistema:

-   **Java JDK 21** o superior.
-   **Apache Maven** 3.8 o superior.
-   **Docker** y **Docker Compose**.

---

## Instalación y Configuración

Sigue estos pasos para configurar el entorno de desarrollo:

1.  **Clonar el Repositorio**:
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd CoopCreditApp
    ```

2.  **Construir los Artefactos**:
    Navega a la raíz de cada microservicio y utiliza Maven para construir el archivo JAR.

    -   **Construir `credit-application-service`**:
        ```bash
        cd CoopCredit
        mvn clean install
        cd ..
        ```
    -   **Construir `risk-central-mock-service`** (si es necesario):
        ```bash
        cd risk-central-mock-service
        mvn clean install
        cd ..
        ```

---

## Cómo Ejecutar la Aplicación

La forma más sencilla y recomendada de ejecutar la aplicación es utilizando **Docker Compose**. Esto levantará todos los servicios necesarios (la aplicación principal, el mock de riesgo y la base de datos PostgreSQL) en contenedores aislados.

Desde la raíz del proyecto (`CoopCreditApp`), ejecuta el siguiente comando:

```bash
docker-compose up --build
```

-   `--build`: Fuerza la reconstrucción de las imágenes de Docker si ha habido cambios en el `Dockerfile` o en el código fuente.

Una vez que los contenedores estén en ejecución, los servicios estarán disponibles en:

-   **credit-application-service**: `http://localhost:8080`
-   **risk-central-mock-service**: `http://localhost:8081`

---

## Cómo Probar la Aplicación

El proyecto tiene una suite de pruebas robusta que cubre las diferentes capas de la aplicación. Para ejecutar todas las pruebas (unitarias, de integración y de persistencia), navega al directorio del microservicio principal y utiliza Maven.

```bash
cd CoopCredit
mvn test
```

-   Las **pruebas unitarias** validan la lógica de dominio de forma aislada y son muy rápidas.
-   Las **pruebas de integración** (`*IntegrationTest`) utilizan `MockMvc` para probar los flujos de la API, incluyendo la seguridad.
-   Las **pruebas de persistencia** (`*JpaAdapterTest`) utilizan **Testcontainers** para levantar una base de datos PostgreSQL temporal, garantizando que las consultas y los mapeos funcionen como en producción.

---

## Endpoints Principales de la API

### Autenticación

-   `POST /auth/register`: Registra un nuevo afiliado y su usuario.
-   `POST /auth/login`: Autentica a un usuario y devuelve un token JWT.

### Solicitudes de Crédito (Requiere Token JWT)

-   `POST /api/v1/applications`: Envía una nueva solicitud de crédito para el afiliado autenticado.

---

## Cumplimiento de Criterios de Aceptación

A continuación, se detalla cómo el proyecto cumple con cada uno de los criterios de evaluación definidos.

| Criterio | Estado | Evidencia en el Código |
| :--- | :--- | :--- |
| **Parte 1: Análisis y Diseño** | | |
| Identificación de Entidades | ✅ Cumplido | Clases en `domain/model` (`Affiliate`, `CreditApplication`, etc.). |
| Identificación de Roles | ✅ Cumplido | `UserRole` enum y su uso en `RegisterAffiliateService`. |
| Análisis de Flujos | ✅ Cumplido | Lógica implementada en `RegisterAffiliateService` y `RegisterCreditApplicationService`. |
| Arquitectura Hexagonal | ✅ Cumplido | Estructura de paquetes `domain`, `application`, `infrastructure`. |
| Identificación de Puertos | ✅ Cumplido | Interfaces en `domain/port/in` y `domain/port/out`. |
| Definición de Casos de Uso | ✅ Cumplido | Clases de servicio como `RegisterAffiliateService` que implementan los puertos de entrada. |
| **Parte 2: Dominio y Persistencia** | | |
| Modelado del Dominio (POJOs) | ✅ Cumplido | Los modelos en `domain/model` no tienen anotaciones de persistencia. |
| Entidades JPA y Relaciones | ✅ Cumplido | Clases en `infrastructure/entity` y migraciones de Flyway `V1` y `V2`. |
| Reglas de Negocio | ✅ Cumplido | `AffiliateValidator` y validaciones dentro de los servicios de dominio. |
| Adaptadores de Persistencia | ✅ Cumplido | Clases `*JpaAdapter` en `infrastructure/adapter/persistence`. |
| Migraciones con Flyway | ✅ Cumplido | Ficheros `V1`, `V2`, `V3` en `resources/db/migration`. |
| Uso de `@Transactional` | ✅ Cumplido | Anotación presente en los métodos de los servicios de dominio que modifican datos. |
| **Parte 3: Seguridad y Errores** | | |
| Autenticación JWT Stateless | ✅ Cumplido | `JwtTokenProvider` y configuración de Spring Security. |
| Endpoints de Registro/Login | ✅ Cumplido | `AuthController` con endpoints `/auth/register` y `/auth/login`. |
| Seguridad por Roles | ✅ Cumplido | `UserRole` enum y configuración de seguridad preparada para la autorización. |
| Validaciones Cruzadas | ✅ Cumplido | Lógica en `RegisterCreditApplicationService` (antigüedad, ratio cuota/ingreso). |
| Manejo de Errores (ProblemDetail) | ✅ Cumplido | Demostrado en `ErrorHandlingIntegrationTest` y configuración global. |
| **Parte 4: Microservicios y Observabilidad** | | |
| Integración con Mock Externo | ✅ Cumplido | `RiskEvaluationPort` y su implementación con un cliente REST. |
| Observabilidad con Actuator | ✅ Cumplido | Configuración en `application.yml` para exponer endpoints `health`, `metrics`, etc. |
| **Parte 5: Pruebas y Docker** | | |
| Pruebas Unitarias de Dominio | ✅ Cumplido | `AffiliateValidatorTest` es un test unitario puro, rápido y aislado. |
| Pruebas de Integración | ✅ Cumplido | `AuthIntegrationTest` utiliza `MockMvc` para probar la API y la seguridad. |
| Testcontainers | ✅ Cumplido | `AffiliateJpaAdapterTest` utiliza Testcontainers para probar la capa de persistencia. |
| Dockerfile Multi-Stage | ✅ Cumplido | El `Dockerfile` en `CoopCredit/` utiliza un patrón de construcción multi-etapa. |
| Docker Compose | ✅ Cumplido | El fichero `docker-compose.yml` orquesta el despliegue de todos los servicios. |
