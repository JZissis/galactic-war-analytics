# Helldivers Galactic War Analytics Platform

A Spring Boot service that ingests live "Galactic War" state from the
[Helldivers 2 Unofficial API](https://api.helldivers2.dev/) (planet control, faction
campaigns, major orders) and turns it into historical analytics: liberation trends,
faction control shifts, and contested-planet tracking.

Built as a portfolio project — see the [Roadmap](#roadmap) for the planned build-out.

## Tech Stack

- Java 21, Spring Boot 4
- Spring Data JPA + PostgreSQL, Flyway migrations
- Spring Security + OAuth2 client
- Resilience4j (circuit breaker / retry) around the external API
- Actuator + Micrometer + Prometheus
- Docker Compose for local infrastructure

## Local Development

Prerequisites: Java 21, Docker.

```bash
docker compose up -d
./mvnw spring-boot:run
```

The app defaults to the `dev` profile (Postgres via docker-compose on `localhost:5432`).
Tests run under the `test` profile (in-memory H2).

## Roadmap

- [x] Phase 0 — Foundation (repo, docker-compose, profiles)
- [x] Phase 1 — External API client (Helldivers 2 API, Resilience4j)
- [ ] Phase 2 — Persistence & scheduled ingestion (time-series war snapshots)
- [ ] Phase 3 — Analytics/domain services
- [ ] Phase 4 — REST API + OpenAPI docs
- [ ] Phase 5 — Security (OAuth2)
- [ ] Phase 6 — Observability (Prometheus/Grafana)
- [ ] Phase 7 — Testing (Testcontainers, WireMock)
- [ ] Phase 8 — CI/CD (GitHub Actions)
- [ ] Phase 9 — Deployment
- [ ] Phase 10 — Frontend (React + TypeScript dashboard, separate follow-up project)