# Truck Booking Platform Backend

A comprehensive Spring Boot backend for a truck booking and management platform.

## Technology Stack

- **Java**: 17
- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL (Neon-compatible)
- **Security**: Spring Security + JWT
- **Build Tool**: Gradle Wrapper
- **ORM**: Spring Data JPA with Hibernate

## Project Structure

```
src/main/java/com/truckplatform/
├── auth/              - JWT authentication and authorization
├── users/             - User management and profiles
├── transporters/      - Transporter registration and management
├── trucks/            - Truck inventory and vehicle management
├── bookings/          - Booking creation and management
├── tracking/          - Real-time tracking and location services
└── common/            - Shared utilities, exceptions, and DTOs
```

## Prerequisites

- Java 17 or higher
- PostgreSQL 12+

## Building the Project

```bash
cd backend
./gradlew build
```

## Running the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080/api`

## Database Configuration

Create a local `.env` file in [`backend/`](/Users/raghvendra/Projects/Truck/backend) before starting the app. You can copy [`backend/.env.example`](/Users/raghvendra/Projects/Truck/backend/.env.example) and fill in your Neon values:

```bash
cp .env.example .env
```

Example `.env`:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://<your-neon-host>/<your-database>?sslmode=require
SPRING_DATASOURCE_USERNAME=<your-neon-user>
SPRING_DATASOURCE_PASSWORD=<your-neon-password>
```

The app imports `.env` from [`application.yml`](/Users/raghvendra/Projects/Truck/backend/src/main/resources/application.yml). If `.env` is missing, it falls back to a local PostgreSQL instance at `jdbc:postgresql://localhost:5432/truck_platform`.

## Key Dependencies

- **Spring Web**: REST API development
- **Spring Data JPA**: Database persistence
- **Spring Security**: Authentication and authorization
- **PostgreSQL Driver**: Database connectivity
- **Lombok**: Reduce boilerplate code
- **JWT (JJWT)**: Token-based authentication

## API Base URL

```
http://localhost:8080/api
```

## Next Steps

1. Create database entities in respective modules
2. Implement repositories extending `JpaRepository`
3. Build service layer for business logic
4. Create REST controllers for API endpoints
5. Configure JWT security filters
6. Implement validation and exception handling

## Development Guidelines

- Follow package structure for better organization
- Use Lombok annotations to reduce code
- Implement proper exception handling
- Add validation annotations to entities
- Document API endpoints with proper comments
- Write unit tests for services
