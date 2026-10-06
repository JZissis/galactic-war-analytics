# Onboarding Guide

Welcome to the project. This guide gets you from zero to a running app, explains how the code
fits together, and covers how we work. For the "what should I study, and in what order" side,
see [LEARNING-PATH.md](LEARNING-PATH.md).

Suggested order for your first week:

1. Read sections 1 and 2 of this guide (what the project is, how it is built).
2. Set up your machine (section 3) and run the app.
3. Walk through one request end to end (section 5) with the code open next to you.
4. Start Module 1 of the learning path in parallel.

---

## 1. What the project is

Helldivers 2 is a co-op game with a shared "Galactic War": every player's missions push planet
liberation up or down. A community-run API, [api.helldivers2.dev](https://api.helldivers2.dev),
exposes the live war state (planets, owners, health, campaigns, major orders).

The live API only shows **now**. This project records that state every few minutes into a
database, so we can answer questions about **history**:

- How fast is a planet being liberated, and will it fall before the deadline?
- Which faction gained or lost territory this week?
- Which planets keep changing hands?

It is a practice and portfolio project. The goal is to learn, and to end up with a clean,
realistic backend that shows real-world skills: external API integration, resilience, persistence,
scheduling, security, observability, testing, CI/CD and deployment.

### Roadmap and where we are

The phases live in the [README roadmap](../README.md#roadmap). The checkboxes there are the source
of truth for progress.

| Phase | Topic | Status |
|-------|-------|--------|
| 0 | Foundation: repo, Docker Compose, Spring profiles | Done |
| 1 | External API client with retries and rate limiting | Done |
| 2 | Database tables + scheduled ingestion of war snapshots | **Next** |
| 3 | Analytics services (trends, control shifts, contested planets) | |
| 4 | Our own REST API + Swagger docs | |
| 5 | Security (OAuth2) | |
| 6 | Observability (Prometheus + Grafana) | |
| 7 | Integration testing (Testcontainers, WireMock) | |
| 8 | CI/CD (GitHub Actions) | |
| 9 | Deployment | |
| 10 | React + TypeScript dashboard (separate project) | |

Phase 2 is a good time to join. Nothing in it is written yet, and it touches the core skills:
SQL, JPA entities, mapping and scheduled jobs.

---

## 2. Tech stack, in plain words

| Tool | What it does for us |
|------|---------------------|
| **Java 25** | The language. Modern Java looks quite different from Java 8; see the learning path. |
| **Spring Boot 4** | Framework that wires the app together, runs the web server and reads config. |
| **Maven** (`mvnw`) | Build tool: downloads libraries, compiles, runs tests and checks. |
| **Spring `RestClient`** + **Apache HttpClient 5** | Make HTTP calls to the Helldivers API. |
| **Jackson** | Converts JSON to Java objects and back. |
| **Resilience4j** | Retries failed calls and limits how fast we call the API. |
| **Lombok** | Generates boilerplate (constructors, getters) from annotations. |
| **PostgreSQL** | The database (runs in Docker). |
| **Spring Data JPA / Hibernate** | Maps Java classes to database tables. |
| **Flyway** | Versioned SQL scripts that create and change the tables. |
| **H2** | In-memory database used only by tests. |
| **Docker Compose** | Starts Postgres locally with one command. |
| **Checkstyle** | Fails the build on style violations (Javadoc, imports). |
| **JUnit + Mockito** | Unit tests. |

---

## 3. Machine setup

Install these once:

1. **Git**: <https://git-scm.com/downloads>. Then set your identity:
   ```bash
   git config --global user.name "Your Name"
   ```
   ```bash
   git config --global user.email "you@example.com"
   ```
2. **JDK 25, exactly, not newer**: any distribution works, for example
   [Eclipse Temurin 25](https://adoptium.net/temurin/releases/?version=25). Lombok hooks into
   compiler internals, so a newer JDK (26+) breaks the build until Lombok catches up. Check with
   `java -version`, and in IntelliJ set both *Project Structure > Project > SDK* and
   *Settings > Build Tools > Maven > Runner > JRE* to 25.
3. **Docker Desktop**: <https://www.docker.com/products/docker-desktop/>.
4. **IntelliJ IDEA** (Community is enough): <https://www.jetbrains.com/idea/>.
   Install the **Lombok** plugin if it is not already bundled, and turn on
   *Settings > Build > Compiler > Annotation Processors > Enable annotation processing*.
5. A GitHub account, so you can be added as a collaborator on
   <https://github.com/JZissis/galactic-war-analytics>.

You do **not** need to install Maven. The repo ships the Maven Wrapper (`mvnw` / `mvnw.cmd`),
which downloads the right version on first use.

### Get the code and run it

```bash
git clone https://github.com/JZissis/galactic-war-analytics.git
```

From the project folder, start the database:

```bash
docker compose up -d
```

Start the app (on Windows PowerShell use `.\mvnw.cmd` instead of `./mvnw`):

```bash
./mvnw spring-boot:run
```

Then call our only endpoint so far:

```bash
curl http://localhost:8080/api/helldivers/current-war-id
```

You should get a number back (the current war season id, for example `801`). The app called the
real Helldivers API to get it.

Other useful commands:

| Command | What it does |
|---------|--------------|
| `./mvnw verify` | Full build: checkstyle, compile, tests. Run this before every push. |
| `./mvnw test` | Run tests only. |
| `./mvnw checkstyle:check` | Run only the style check. |
| `docker compose down` | Stop Postgres (data is kept in a Docker volume). |
| `docker compose down -v` | Stop Postgres **and delete its data**. |

### Troubleshooting

| Symptom | Cause and fix |
|---------|---------------|
| `Fatal error compiling: java.lang.ExceptionInInitializerError: com.sun.tools.javac.tree.EndPosTable` (or another `com.sun.tools.javac...` class) | Maven runs on a JDK newer than 25, and Lombok does not support that JDK yet. Run `./mvnw -v`, check the `Java version:` line, and switch `JAVA_HOME` and the IntelliJ Maven runner JRE to JDK 25. |
| `WARNING: sun.misc.Unsafe::objectFieldOffset has been called by lombok.permit.Permit` | Harmless. Lombok uses an API that the JDK will remove later. Ignore it. |
| Checkstyle violations fail the build | Read the file and line in the message. Common ones: missing Javadoc, or a `java.*` import (see Conventions). |
| App fails at startup with a connection error to `localhost:5432` | Postgres is not running. Run `docker compose up -d`. |

---

## 4. Project tour

All Java code lives under
`src/main/java/gr/ioanniszisis/helldivers/galactic/war/analytics/platform/`
(shortened to `.../platform/` below).

```
.../platform/
├── GalacticWarAnalyticsApplication.java   Entry point (main method)
├── config/
│   ├── client/                            How we call the Helldivers API
│   │   ├── HellDiversApiRestClientConfig  Builds the HTTP client bean
│   │   ├── HelldiversApiResilienceConfig  Retry + rate limiter rules
│   │   └── net/
│   │       ├── GenericRestClient          Wrapper: every GET goes through retry + rate limit
│   │       ├── HttpClientFactory          Apache HttpClient pool and timeouts
│   │       └── RateLimitInterceptor       Logs the API's rate-limit headers
│   ├── exception/                         Our exceptions + GlobalExceptionHandler
│   ├── properties/                        Java classes bound to application.yaml values
│   └── security/WebMvcSecurityConfig      Allows all requests for now (until Phase 5)
├── model/helldivers/api/dto/response/
│   ├── raw/                               DTOs for /raw/... endpoints (ArrowHead's own format)
│   ├── v1/                                DTOs for /api/v1/... endpoints (community format)
│   └── v2/                                DTOs for /api/v2/... endpoints
├── service/helldivers/api/
│   └── HelldiversApiService               Business-facing methods ("get current war id")
└── web/controller/
    └── HelldiversApiController            Our HTTP endpoints, under /api/helldivers
```

Other important files:

| File | Purpose |
|------|---------|
| [pom.xml](../pom.xml) | Dependencies and build plugins. |
| [application.yaml](../src/main/resources/application.yaml) | Shared config: API base URL, endpoint paths, timeouts. |
| [application-dev.yaml](../src/main/resources/application-dev.yaml) | Local profile (default): Postgres on `localhost:5432`. |
| [application-test.yaml](../src/main/resources/application-test.yaml) | Test profile: in-memory H2, Flyway off. |
| [application-prod.yaml](../src/main/resources/application-prod.yaml) | Production profile: DB settings from environment variables. |
| [Helldivers-2-API.json](../src/main/resources/json/Helldivers-2-API.json) | Saved OpenAPI spec of the external API. Check it before writing a new DTO. |
| [docker-compose.yml](../docker-compose.yml) | Local Postgres. |
| [checkstyle.xml](../checkstyle.xml) | Style rules the build enforces. |

### Layers

The code follows the classic layered layout. Each layer only talks to the one below it:

```
HTTP request
   │
   ▼
Controller   (web/controller)   — HTTP in/out only, no logic
   │
   ▼
Service      (service)          — what the app does
   │
   ▼
Client       (config/client)    — how we reach the outside world, incl. retries and rate limits
   │
   ▼
Helldivers API (api.helldivers2.dev)
```

From Phase 2 on, a **repository** layer (database access) sits next to the client, and a
**scheduler** triggers the service every 5 minutes instead of an HTTP request.

---

## 5. One request, end to end

Follow `GET /api/helldivers/current-war-id` with the code open. This is the best way to
understand the whole app.

1. **`HelldiversApiController.getCurrentWarId()`** receives the request. Spring found this class
   because of `@RestController` and routed the URL because of `@RequestMapping` + `@GetMapping`.
   The service was injected through the constructor that Lombok's `@RequiredArgsConstructor`
   generates.
2. **`HelldiversApiService.getCurrentWarId()`** reads the endpoint path from config
   (`helldivers-api-rest.endpoints.raw.current-war-id-url` in `application.yaml`) and calls the
   client. It wraps the body in an `Optional` and throws `HelldiversApiEmptyResponseException` if
   the API returned nothing.
3. **`GenericRestClient.get()`** builds the HTTP call and runs it through `execute()`, which wraps
   it in two Resilience4j decorators:
   - **RateLimiter**: at most 4 calls per 10 seconds (the API allows 5). Extra calls wait.
   - **Retry**: up to 3 attempts for timeouts, 429 and 502/503/504, with 1s then 2s backoff.
     After a 429 it waits as long as the API's `Retry-After` header says.
4. **`HellDiversApiRestClientConfig`** built that client at startup: base URL, the
   `X-Super-Client` / `X-Super-Contact` headers the API asks every client to send, the logging
   interceptor, and a handler that turns HTTP 429 into `HelldiversApiRateLimitException`.
5. **Jackson** turns the JSON `{"id": 801}` into the `WarId` record.
6. If anything fails, **`GlobalExceptionHandler`** turns the exception into a standard error
   body ([RFC 9457 Problem Details](https://www.rfc-editor.org/rfc/rfc9457)):

   | Exception | Status we return | Why |
   |-----------|------------------|-----|
   | Rate limited (ours or theirs) | 503 + `Retry-After` | Upstream is busy; the caller did nothing wrong. |
   | `ResourceAccessException` (timeout, no connection) | 504 | Upstream did not answer in time. |
   | `RestClientResponseException` | 502 | Upstream answered with an error. |
   | `HelldiversApiEmptyResponseException` | 502 | Upstream answered with nothing. |
   | Anything else | 500 | Our bug. Stack trace goes to the log. |

**Exercise:** put a breakpoint in each of these classes, start the app in debug mode from
IntelliJ, call the endpoint, and step through it.

---

## 6. Conventions

The build enforces some of these; the others are agreed habits.

- **Java imports.** JDK types come only from `import module java.base;`. Never write
  `import java.util.List;`, wildcard `java.*` imports or static `java.*` imports; write
  `Objects.nonNull(x)` instead of a static import. Third-party imports (Spring, Lombok, ...) stay
  normal single-type imports. Checkstyle fails the build otherwise.
- **Javadoc** on every public class and method in `src/main`. Checkstyle enforces it. Tests are
  exempt.
- **DTOs are records** with `@JsonIgnoreProperties(ignoreUnknown = true)`, so new fields in the
  API do not break us. Numbers that can be missing are boxed (`Long`, not `long`), because the
  JSON library fails on `null` for primitives.
- **Endpoint paths live in YAML**, never hard-coded in Java.
- **Resilience stays in the client layer.** Services never deal with retries or rate limits.
- **JPA entities are separate from DTOs** (from Phase 2). A plain hand-written mapper converts
  between them. Entities use Lombok `@Getter`/`@Setter` and a protected no-args constructor,
  never `@Data`.
- **Database changes go through Flyway.** Never edit a migration that was already applied;
  add a new `V<n>__description.sql` instead.
- **Keep it simple.** No interface with only one implementation, no "for later" code. Add things
  when a real need shows up.

---

## 7. How we work together

### Git workflow

Until now everything went straight to `main`. With two people, use branches and pull requests:

1. Update main: `git switch main` then `git pull`.
2. Create a branch: `git switch -c phase-2/planet-entities` (short, descriptive name).
3. Commit in small steps. Message style: short imperative subject, `Phase N:` prefix when the work
   belongs to a phase, for example `Phase 2: Add planet and campaign migrations`.
4. Run `./mvnw verify` before pushing.
5. Push and open a pull request on GitHub. The other person reviews it.
6. Merge after approval, then delete the branch.

Reviewing each other's PRs is one of the best learning tools here. Ask "why" in review comments.

### Design first, then code

Each phase starts with a short design discussion (which tables, which classes, which edge
cases). Decisions get written down before coding starts. Then the code is written step by step,
one commit per step.

### Using AI assistants

The project uses Claude Code as a **tutor and reviewer**, not as the author. The rule of thumb:
let it explain a design or review your code, but write the implementation yourself. That is
where the learning happens. If you ask it to write something, read every line until you could
have written it.

---

## 8. Phase 2 at a glance

What we build next, so you know where the code is heading:

- **Source:** four v1 endpoints every run: `/api/v1/planets`, `/api/v1/campaigns`,
  `/api/v1/war`, `/api/v1/assignments` (major orders).
- **Schedule:** every 5 minutes, using Spring's `@Scheduled(fixedDelay = ...)`. Interval and
  an on/off flag live in YAML.
- **Storage:** reference tables (`planet`, `campaign`, `major_order`, `major_order_task`) and
  time-series snapshot tables (`planet_snapshot`, `war_snapshot`, `major_order_progress`).
- **Change-only snapshots:** we write a new planet row only when owner, health, players or event
  changed since the last stored row. This keeps the table small.
- **Timestamps:** from our own clock, because the API's `War.now` field is wrong.
- **Error handling:** the scheduler has its own try/catch and logging. `GlobalExceptionHandler`
  only covers HTTP requests.
- **Circuit breaker:** added around the client, so a dead API does not get hammered.

Steps, one commit each:

1. Flyway migrations + JPA entities + repositories
2. Service methods that fetch the four endpoints
3. Mapper + ingestion service + unit test
4. Scheduler + config
5. Circuit breaker + test
6. README checkbox + docs

Good first tasks for you: step 2 (fetch methods follow the existing `getCurrentWarId()`
pattern) and the mapper in step 3 (plain Java, easy to unit test).

---

## 9. Glossary

| Term | Meaning |
|------|---------|
| **Bean** | An object Spring creates and manages. You ask for one in a constructor and Spring passes it in. |
| **Dependency injection (DI)** | Classes receive their collaborators instead of creating them with `new`. |
| **DTO** | Data Transfer Object. A plain data class that mirrors JSON we send or receive. |
| **Entity** | A Java class mapped to a database table by JPA. |
| **Repository** | Spring Data interface that gives you database queries without writing SQL. |
| **Migration** | A versioned SQL script Flyway runs once, in order, to build the schema. |
| **Profile** | A named set of config (`dev`, `test`, `prod`) picked at startup. |
| **Rate limit** | Max number of calls allowed in a time window. The Helldivers API allows 5 per 10 s. |
| **Retry / backoff** | Try again after a failure, waiting longer each time. |
| **Circuit breaker** | Stops calling a failing service for a while, so it can recover. |
| **Snapshot** | One row that records the state of something at a point in time. |
| **raw / v1 / v2** | The three API flavours: raw is ArrowHead's own format (slow, odd), v1/v2 are the cleaner community format. We prefer v1. |
| **Major order** | The game's current global objective for all players. |
| **Campaign** | An active fight on a planet. |
| **ArrowHead (AH)** | The studio that makes Helldivers 2. |
