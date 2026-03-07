# Truck Booking Platform Backend

A comprehensive Spring Boot backend for a truck booking and management platform.

## Technology Stack

- **Java**: 17
- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **Build Tool**: Maven
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
- Maven 3.6+
- PostgreSQL 12+

## Building the Project

```bash
cd backend
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

## Database Configuration

Update `src/main/resources/application.yml` with your PostgreSQL credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/truck_platform
    username: postgres
    password: your_password
```

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
