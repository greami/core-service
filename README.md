Core - microservice

Modules:
- domain: core domain records and ports
- application: use-cases and mappers
- adapter: Spring Boot application, adapters, infra

Run:
  mvn -T 1C clean install
  mvn -pl adapter spring-boot:run

Docker:
  1. Start Docker Desktop
  2. docker compose up --build
  3. open http://localhost:8080
  4. docker compose down

Notes:
- Update secrets and versions before production
- openapi-generator configured to read src/main/resources/openapi.yaml
