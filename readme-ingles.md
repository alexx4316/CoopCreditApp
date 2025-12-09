# CoopCredit - Credit Application and Evaluation System

This project is an implementation of a microservices system for **CoopCredit**, a savings and credit cooperative. The system digitizes and automates the credit application and evaluation process, following industry best practices such as Hexagonal Architecture, JWT security, and Docker containerization.

## Table of Contents
1.  [Architecture](#architecture)
2.  [Technologies Used](#technologies-used)
3.  [Project Structure](#project-structure)
4.  [Prerequisites](#prerequisites)
5.  [Installation and Configuration](#installation-and-configuration)
6.  [How to Run the Application](#how-to-run-the-application)
7.  [How to Test the Application](#how-to-test-the-application)
8.  [Main API Endpoints](#main-api-endpoints)
9.  [Compliance with Acceptance Criteria](#compliance-with-acceptance-criteria)

---

## Architecture

The project is designed following the principles of **Hexagonal Architecture (Ports and Adapters)**. This architecture promotes low coupling and high cohesion, isolating the domain's business logic from infrastructure technologies.

-   **Domain**: Contains the pure business logic, models (POJOs), and validation rules. It has no external framework dependencies.
-   **Application**: Orchestrates business flows (use cases) and defines input ports.
-   **Infrastructure**: Implements output ports and contains technological details such as REST controllers, security configuration, persistence adapters (JPA), and clients for external services.

---

## Technologies Used

-   **Language**: Java 21
-   **Framework**: Spring Boot 3
-   **Database**: PostgreSQL
-   **Migration Management**: Flyway
-   **Security**: Spring Security with JWT (JSON Web Tokens)
-   **Containerization**: Docker and Docker Compose
-   **Dependency Management**: Maven
-   **Testing**:
    -   JUnit 5
    -   MockMvc for API integration tests.
    -   Testcontainers for persistence tests with a real database.
-   **Error Handling**: ProblemDetail (RFC 7807)
-   **Observability**: Spring Boot Actuator and Micrometer (with Prometheus endpoint)

---

## Project Structure

The `CoopCreditApp` repository contains two main microservices:

1.  `CoopCredit`: The main service that manages affiliates and credit applications.
2.  `risk-central-mock-service`: A mock that simulates an external risk evaluation service.

```
CoopCreditApp/
├── CoopCredit/                 # Main Microservice
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/CoopCredit/CoopCredit/
│   │   │   │   ├── application/  # REST Controllers and DTOs
│   │   │   │   ├── domain/       # Models, Ports, and Domain Services
│   │   │   │   └── infrastructure/ # Adapters, JPA Entities, Mappers, Security
│   │   │   └── resources/
│   │   │       ├── db/migration/ # Flyway Scripts (V1, V2, V3)
│   │   │       └── application.yml
│   │   └── test/                 # Unit and Integration Tests
│   ├── Dockerfile                # Multi-stage Dockerfile
│   └── pom.xml
├── risk-central-mock-service/  # Risk Service Mock
└── docker-compose.yml          # Orchestrates the deployment of all services
```

---

## Prerequisites

To run this project, you need to have the following installed on your system:

-   **Java JDK 21** or higher.
-   **Apache Maven** 3.8 or higher.
-   **Docker** and **Docker Compose**.

---

## Installation and Configuration

Follow these steps to set up your development environment:

1.  **Clone the Repository**:
    ```bash
    git clone <REPOSITORY_URL>
    cd CoopCreditApp
    ```

2.  **Build the Artifacts**:
    Navigate to the root of each microservice and use Maven to build the JAR file.

    -   **Build `credit-application-service`**:
        ```bash
        cd CoopCredit
        mvn clean install
        cd ..
        ```
    -   **Build `risk-central-mock-service`** (if necessary):
        ```bash
        cd risk-central-mock-service
        mvn clean install
        cd ..
        ```

---

## How to Run the Application

The easiest and recommended way to run the application is by using **Docker Compose**. This will bring up all necessary services (the main application, the risk mock, and the PostgreSQL database) in isolated containers.

From the root of the project (`CoopCreditApp`), execute the following command:

```bash
docker-compose up --build
```

-   `--build`: Forces a rebuild of Docker images if there have been changes to the `Dockerfile` or source code.

Once the containers are running, the services will be available at:

-   **credit-application-service**: `http://localhost:8080`
-   **risk-central-mock-service**: `http://localhost:8081`

---

## How to Test the Application

The project has a robust test suite covering different layers of the application. To run all tests (unit, integration, and persistence), navigate to the main microservice directory and use Maven.

```bash
cd CoopCredit
mvn test
```

-   **Unit tests** validate domain logic in isolation and are very fast.
-   **Integration tests** (`*IntegrationTest`) use `MockMvc` to test API flows, including security.
-   **Persistence tests** (`*JpaAdapterTest`) use **Testcontainers** to spin up a temporary PostgreSQL database, ensuring that queries and mappings work as in production.

---

## Main API Endpoints

### Authentication

-   `POST /auth/register`: Registers a new affiliate and their user.
-   `POST /auth/login`: Authenticates a user and returns a JWT token.

### Credit Applications (Requires JWT Token)

-   `POST /api/v1/applications`: Submits a new credit application for the authenticated affiliate.

---

## Compliance with Acceptance Criteria

Below is a detailed explanation of how the project complies with each of the defined evaluation criteria.

| Criterion | Status | Evidence in Code |
| :--- | :--- | :--- |
| **Part 1: Analysis and Design** | | |
| Entity Identification | ✅ Compliant | Classes in `domain/model` (`Affiliate`, `CreditApplication`, etc.). |
| Role Identification | ✅ Compliant | `UserRole` enum and its use in `RegisterAffiliateService`. |
| Flow Analysis | ✅ Compliant | Logic implemented in `RegisterAffiliateService` and `RegisterCreditApplicationService`. |
| Hexagonal Architecture | ✅ Compliant | Package structure `domain`, `application`, `infrastructure`. |
| Port Identification | ✅ Compliant | Interfaces in `domain/port/in` and `domain/port/out`. |
| Use Case Definition | ✅ Compliant | Service classes like `RegisterAffiliateService` implementing input ports. |
| **Part 2: Domain and Persistence** | | |
| Domain Modeling (POJOs) | ✅ Compliant | Models in `domain/model` have no persistence annotations. |
| JPA Entities and Relationships | ✅ Compliant | Classes in `infrastructure/entity` and Flyway migrations `V1` and `V2`. |
| Business Rules | ✅ Compliant | `AffiliateValidator` and validations within domain services. |
| Persistence Adapters | ✅ Compliant | `*JpaAdapter` classes in `infrastructure/adapter/persistence`. |
| Flyway Migrations | ✅ Compliant | Files `V1`, `V2`, `V3` in `resources/db/migration`. |
| Use of `@Transactional` | ✅ Compliant | Annotation present in methods of critical domain services that modify data. |
| **Part 3: Security and Error Handling** | | |
| Stateless JWT Authentication | ✅ Compliant | `JwtTokenProvider` and Spring Security configuration. |
| Register/Login Endpoints | ✅ Compliant | `AuthController` with `/auth/register` and `/auth/login` endpoints. |
| Role-Based Security | ✅ Compliant | `UserRole` enum and security configuration ready for authorization. |
| Cross-Field Validations | ✅ Compliant | Logic in `RegisterCreditApplicationService` (affiliate seniority, debt-to-income ratio). |
| Standardized Error Handling (ProblemDetail) | ✅ Compliant | Demonstrated in `ErrorHandlingIntegrationTest` and global configuration. |
| **Part 4: Microservices and Observability** | | |
| External Mock Integration | ✅ Compliant | `RiskEvaluationPort` and its implementation with a REST client. |
| Observability with Actuator | ✅ Compliant | Configuration in `application.yml` to expose `health`, `metrics`, etc. endpoints. |
| **Part 5: Testing and Docker** | | |
| Domain Unit Tests | ✅ Compliant | `AffiliateValidatorTest` is a pure, fast, and isolated unit test. |
| Integration Tests | ✅ Compliant | `AuthIntegrationTest` uses `MockMvc` to test the API and security. |
| Testcontainers | ✅ Compliant | `AffiliateJpaAdapterTest` uses Testcontainers to test the persistence layer. |
| Multi-Stage Dockerfile | ✅ Compliant | The `Dockerfile` in `CoopCredit/` uses a multi-stage build pattern. |
| Docker Compose | ✅ Compliant | The `docker-compose.yml` file orchestrates the deployment of all services. |
