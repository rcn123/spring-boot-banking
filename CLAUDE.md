# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

```bash
# Build the application (skip tests for faster builds)
./mvnw clean install -DskipTests=true

# Run tests
./mvnw test

# Run the application
java -jar target/Banking-0.0.1.jar

# Run with Docker
docker build -t "spring-boot:banking" .
docker run -p 8080:8080 spring-boot:banking
```

## Architecture Overview

This is a Spring Boot 2.7.0 banking microservice with a layered architecture:

- **Controllers** (`/controllers/`) - REST endpoints for account and transaction operations
- **Services** (`/services/`) - Business logic for account management and transaction processing
- **Models** (`/models/`) - JPA entities for Account and Transaction
- **Repositories** (`/repositories/`) - Data access layer using Spring Data JPA
- **Utils** (`/utils/`) - Input validation, code generation, and data transfer objects

The application uses H2 in-memory database with pre-loaded sample data. Database console available at `/h2-console` when running locally.

## API Structure

All endpoints are under `/api/v1` base path:

**Account Operations:**
- `POST /accounts` - Check balance (requires sortCode + accountNumber)
- `PUT /accounts` - Create account (requires bankName + ownerName)

**Transaction Operations:**
- `POST /transactions` - Transfer between accounts
- `POST /deposit` - Deposit to account
- `POST /withdraw` - Withdraw from account

## Key Technical Details

- **Database**: H2 in-memory with schema/data initialization from `/resources/schema.sql` and `/resources/data.sql`
- **Code Generation**: Uses `generex` library to generate sort codes and account numbers from regex patterns
- **Validation**: Comprehensive input validation using Spring Validation annotations
- **Testing**: Separate unit tests (`/test/unit/`) and integration tests (`/test/integration/`) using JUnit 5 + Mockito
- **Configuration**: Application settings in `/resources/application.yaml` with `local` profile active

## Development Notes

- Package structure follows `com.example.paul.*` convention
- Sort codes and account numbers are auto-generated using regex patterns in `CodeGenerator`
- Transaction processing includes balance validation and automatic timestamp generation
- Postman collection provided in `Banking.postman_collection.json` for API testing
- CI/CD configured via GitHub Actions with Maven build and Java 17