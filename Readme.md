# 💳 Bank Account Management System

A Spring Boot application for managing Nigerian bank accounts (NUBAN-compliant), designed for internal bank systems and third-party APIs. It supports account creation, validation, and retrieval using official CBN (Central Bank of Nigeria) standards.

---

## 🧩 Features

- ✅ **Create NUBAN-compliant bank accounts**
- ✅ **Generate valid 10-digit account numbers** with CBN's checksum logic
- ✅ **Validate if an account exists** via external provider or DB
- ✅ **Retrieve account details** by account number
- ✅ **Persist accounts** in H2 in-memory database
- ✅ **Swagger/OpenAPI documentation**
- ✅ **Secure endpoints with Spring Security**
- ✅ **H2 Console** enabled for dev inspection

---

## 📁 Project Structure
C:.
├───.gradle
│   ├───8.14.2
│   │   ├───checksums
│   │   ├───executionHistory
│   │   ├───expanded
│   │   ├───fileChanges
│   │   ├───fileHashes
│   │   └───vcsMetadata
│   ├───buildOutputCleanup
│   └───vcs-1
├───.idea
│   └───modules
├───build
│   ├───classes
│   │   └───java
│   │       ├───main
│   │       │   └───com
│   │       │       └───example
│   │       │           └───bemojr
│   │       │               ├───configuration
│   │       │               ├───controller
│   │       │               ├───entity
│   │       │               ├───enumeration
│   │       │               ├───repository
│   │       │               ├───service
│   │       │               └───serviceImpl
│   │       └───test
│   │           └───com
│   │               └───example
│   │                   └───bemojr
│   ├───generated
│   │   └───sources
│   │       ├───annotationProcessor
│   │       │   └───java
│   │       │       ├───main
│   │       │       └───test
│   │       └───headers
│   │           └───java
│   │               ├───main
│   │               └───test
│   ├───libs
│   ├───reports
│   │   └───tests
│   │       └───test
│   │           ├───classes
│   │           ├───css
│   │           ├───js
│   │           └───packages
│   ├───resources
│   │   └───main
│   │       ├───static
│   │       └───templates
│   ├───test-results
│   │   └───test
│   │       └───binary
│   └───tmp
│       ├───bootJar
│       ├───compileJava
│       ├───compileTestJava
│       ├───jar
│       └───test
├───gradle
│   └───wrapper
└───src
├───main
│   ├───java
│   │   └───com
│   │       └───example
│   │           └───bemojr
│   │               ├───configuration
│   │               ├───controller
│   │               ├───entity
│   │               ├───enumeration
│   │               ├───repository
│   │               ├───service
│   │               └───serviceImpl
│   └───resources
│       ├───static
│       └───templates
└───test
└───java
└───com
└───example
└───bemojr

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17+
- Gradle 8+
- Postman or any REST client (for testing)
- (Optional) IDE like IntelliJ or VS Code

### 🔧 Running the App

```bash
./gradlew bootRun

```
## 🔐 Security Configuration
CSRF is disabled for ease of testing.

These paths are publicly accessible:

- #### /api/v*/bank-account/**

- #### /api/v*/user/**

- #### /swagger-ui/**, /v*/api-docs/**

- #### /h2-console/**

## 💾 In-Memory Database (H2)
#### Accessible at: http://localhost:8080/h2-console

#### JDBC URL: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1

#### Username: sa (no password)

#### Enable frames via security config to allow H2 to render.

## 🧠 NUBAN Account Generation
#### NUBAN (Nigeria Uniform Bank Account Number) generation follows CBN spec:

#### Uses a 3-digit bank code (e.g. 122339)

#### Generates 9-digit base account number

#### Computes 10th digit as a checksum using weighted algorithm

#### Ensures uniqueness across both internal DB and external provider

#### String nuban = generateNubanAccountNumber("122339"); // e.g., banky

## 📫 REST API Endpoints
#### 🔹 Create New Account
- POST /api/v1/bank-account/create

#### Request Body (excluding account number — it will be generated):
```
{
  "firstName": "John",
  "lastName": "Doe",
  "otherName": "A."
}
```

#### 🔹 Get Account Details
GET /api/v1/bank-account/details?accountNumber=1234567890&bankCode=122339

#### 🔹 Validate Account Number
GET /api/v1/bank-account/validate?accountNumber=1234567890&bankCode=122339

## 🔍 API Docs
### Swagger UI:
➡ http://localhost:8080/swagger-ui/index.html