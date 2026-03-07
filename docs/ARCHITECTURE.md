System Architecture

Frontend:
ReactJS SPA communicating with backend APIs.

Backend:
Spring Boot application structured into modules:

- auth
- users
- transporters
- trucks
- bookings
- tracking
- ratings
- notifications

Database:
PostgreSQL

Caching:
Redis (future)

Messaging:
Kafka or RabbitMQ (future)

Deployment:
Docker
GitHub Actions
Cloud hosting