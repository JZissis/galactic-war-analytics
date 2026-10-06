# Learning Path

A study plan that runs in parallel with the project. Each module says **why it matters here**,
**where it shows up in our code**, **what to study** and **a small exercise**. The modules are
ordered so that you learn each topic just before the project needs it.

Rough time budget: 5–8 hours a week. Do not try to finish a module before touching the
project. Read a bit, then look at the matching code, then come back.

Setup and project overview are in [ONBOARDING.md](ONBOARDING.md).

## Overview

| # | Module | Needed for | Est. time |
|---|--------|------------|-----------|
| 1 | Modern Java refresh | Everything | 2 weeks |
| 2 | Git and GitHub | Working together | 2–3 days |
| 3 | HTTP, REST and JSON | Phase 1 code, Phase 4 | 3–4 days |
| 4 | Maven and the build | Running and fixing builds | 1–2 days |
| 5 | Spring Boot core | Everything | 1–2 weeks |
| 6 | Calling external APIs + resilience | Phase 1 code, Phase 2 | 1 week |
| 7 | SQL and PostgreSQL | Phase 2 | 1 week |
| 8 | JPA, Hibernate and Flyway | Phase 2 | 1–2 weeks |
| 9 | Scheduling and error handling | Phase 2 | 2–3 days |
| 10 | Testing | Phase 2 onward, Phase 7 | 1 week |
| 11 | Docker | Phase 6, Phase 9 | 3–4 days |
| 12 | Later phases | Phases 4–9 | When we get there |

Main reference sites used below:

- [dev.java/learn](https://dev.java/learn/): official Java tutorials, up to date.
- [Spring Guides](https://spring.io/guides): short hands-on tutorials.
- [Spring Boot reference](https://docs.spring.io/spring-boot/index.html): the official docs.
- [Baeldung](https://www.baeldung.com/): practical articles on almost every Java/Spring topic.
  Search "baeldung <topic>".

---

## Module 1 — Modern Java refresh

**Why:** Java changed a lot after Java 8. Our code uses records, `var`, lambdas, `Optional`,
pattern matching and module imports everywhere. If you last used Java 6–8, most of the syntax
will look new.

**Where in our code:** every DTO is a `record`; `GenericRestClient` uses lambdas and
`Supplier<T>`; `HelldiversApiService` uses `Optional`; `GlobalExceptionHandler` uses a pattern
`switch`; every file starts with `import module java.base;`.

**Study, in this order:**

1. Basics again if needed: classes, interfaces, generics, collections (`List`, `Map`, `Set`).
   [dev.java: Learn the basics](https://dev.java/learn/)
2. Lambdas and functional interfaces (`Supplier`, `Function`, `Predicate`), method references
   (`UriBuilder::build`).
3. Streams: `map`, `filter`, `collect`, `toList()`.
4. `Optional`: `ofNullable`, `map`, `orElseThrow`. Never use it for fields or parameters.
5. `var` for local variables.
6. Records ([JEP 395](https://openjdk.org/jeps/395)).
7. Text blocks (`"""`), switch expressions, `instanceof` pattern matching.
8. Pattern matching for `switch` ([JEP 441](https://openjdk.org/jeps/441)) and record patterns.
9. Unnamed variables `_` ([JEP 456](https://openjdk.org/jeps/456)): used for unused catch and
   lambda parameters in our code.
10. Module import declarations ([JEP 511](https://openjdk.org/jeps/511)): our import style.
11. Virtual threads ([JEP 444](https://openjdk.org/jeps/444)): just the idea. Spring turns them
    on for us (`spring.threads.virtual.enabled` in `application.yaml`).
12. Exceptions: checked vs unchecked, why Spring code uses unchecked ones.

**Exercise:** write a small standalone program (no Spring) with a `record Planet(int index,
String name, String owner, long health)`. Make a `List<Planet>`, then with streams: count planets
per owner, find the planet with the lowest health, and return an `Optional<Planet>` lookup by
name. Use a pattern `switch` over a `sealed interface Faction` with three record
implementations.

---

## Module 2 — Git and GitHub

**Why:** we work on the same repo. Branches, pull requests and reviews are how we avoid stepping
on each other.

**Study:**

- [Pro Git book](https://git-scm.com/book/en/v2), chapters 1–3 (basics, branching).
- [GitHub Docs: About pull requests](https://docs.github.com/en/pull-requests).
- Commands to know cold: `status`, `add`, `commit`, `switch`, `pull`, `push`, `log --oneline`,
  `diff`, `merge`, `rebase` (basic), `stash`.

**Exercise:** create a branch, add your name to a scratch file, push, open a PR, get it
reviewed, then close it without merging. Then practise one merge conflict on purpose.

---

## Module 3 — HTTP, REST and JSON

**Why:** the whole app is "call an HTTP API, store the result, expose our own HTTP API".

**Where in our code:** `GenericRestClient` (outgoing calls), `HelldiversApiController`
(incoming calls), the DTO records (JSON shape), `GlobalExceptionHandler` (status codes).

**Study:**

- [MDN: An overview of HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview):
  methods, headers, status codes.
- [MDN: HTTP response status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status):
  know 200, 201, 204, 400, 401, 403, 404, 429, 500, 502, 503, 504.
- What REST means in practice: resources as URLs, HTTP verbs, statelessness.
- JSON syntax and how it maps to Java objects.
- [RFC 9457 Problem Details](https://www.rfc-editor.org/rfc/rfc9457): our error format. Skim.

**Exercise:** open <https://api.helldivers2.dev/api/v1/planets> in a browser or with `curl`.
Find one planet, then find the matching fields in our
[`Planet` record](../src/main/java/gr/ioanniszisis/helldivers/galactic/war/analytics/platform/model/helldivers/api/dto/response/v1/Planet.java).
Then look up the same endpoint in our saved
[OpenAPI spec](../src/main/resources/json/Helldivers-2-API.json). Optional: install
[Bruno](https://www.usebruno.com/) or Postman and save a few requests.

---

## Module 4 — Maven and the build

**Why:** you will run the build many times a day, and sometimes it fails for reasons that are
not in your code (checkstyle, a missing dependency).

**Where:** [pom.xml](../pom.xml), `mvnw`, [checkstyle.xml](../checkstyle.xml).

**Study:**

- [Maven in 5 minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).
- [Introduction to the build lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html):
  `validate` → `compile` → `test` → `package` → `verify`. Our checkstyle runs in `validate`, so
  it fails first.
- Dependencies, scopes (`compile`, `runtime`, `test`), the Spring Boot parent POM (it picks the
  versions for us, which is why most dependencies have no `<version>`).

**Exercise:** read our `pom.xml` and, for each dependency, write one line on why we need it.
Then add `import java.util.List;` to any class, run `./mvnw verify`, read the checkstyle error,
and revert.

---

## Module 5 — Spring Boot core

**Why:** Spring wires the whole app. Most "magic" in the code is Spring doing dependency
injection and reading config.

**Where:** every class with `@Configuration`, `@Bean`, `@Service`, `@RestController`; the
`config/properties` classes bound to YAML; the `application-*.yaml` profiles.

**Study:**

1. [Spring Guide: Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/).
   Do it hands-on in a separate scratch project.
2. Inversion of control and dependency injection: beans, `@Component` / `@Service`,
   constructor injection. Why we use `@RequiredArgsConstructor` + `final` fields instead of
   `@Autowired` on fields.
3. `@Configuration` + `@Bean`: creating beans by hand (see `HellDiversApiRestClientConfig`).
4. Externalized configuration: `application.yaml`, `@ConfigurationProperties`, profiles.
   [Spring Boot docs: Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html).
5. Spring MVC basics: `@RestController`, `@GetMapping`, `@PathVariable`, `@RequestParam`,
   `ResponseEntity`.
6. Lombok: `@RequiredArgsConstructor`, `@Getter`, `@Setter`, `@Slf4j`, `@UtilityClass`.
   [Lombok features](https://projectlombok.org/features/). Know what code each one generates.
7. Logging with SLF4J: levels and why we log at `DEBUG` in dev.

**Exercise:** in the project (on a branch, not to be merged), add an endpoint
`GET /api/helldivers/hello?name=X` that returns a greeting, with the greeting prefix read from
`application.yaml` through a `@ConfigurationProperties` class. Then remove it.

---

## Module 6 — Calling external APIs and resilience

**Why:** the external API is slow, sometimes down, and rate limited. Phase 1 was all about
handling that. Phase 2 adds four new calls and a circuit breaker.

**Where:** everything under `config/client/`, plus
[HelldiversApiResilienceConfigTest](../src/test/java/gr/ioanniszisis/helldivers/galactic/war/analytics/platform/config/client/HelldiversApiResilienceConfigTest.java).

**Study:**

- [Spring Framework docs: REST Clients](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html),
  the `RestClient` section.
- Jackson basics: how a record maps to JSON, `@JsonIgnoreProperties`, `@JsonProperty`.
- [Resilience4j docs](https://resilience4j.readme.io/docs): read **Retry**, **RateLimiter**
  and **CircuitBreaker**.
- Concepts: timeouts (connect vs read), connection pools, idempotency, exponential backoff, why
  you do not retry a 500.
- The [Helldivers API README](https://github.com/helldivers-2/api): rate limits and the
  `X-Super-Client` header.

**Exercise:** read `HelldiversApiResilienceConfig` and answer: with the API completely hung, how
long does one call take before we give up, and why? (Answer is in the code comments; work it
out first.) Then read the test and add one case: a 429 with `Retry-After: 2` waits 2 seconds
before the retry.

---

## Module 7 — SQL and PostgreSQL

**Why:** Phase 2 stores everything in Postgres, and Phase 3 analytics are mostly SQL queries
over time-series data.

**Study:**

- [SQLBolt](https://sqlbolt.com/): interactive lessons, do all of them.
- [PostgreSQL tutorial](https://www.postgresql.org/docs/current/tutorial.html), parts I–II.
- Topics to be solid on: `CREATE TABLE`, data types (`bigint`, `double precision`, `text`,
  `timestamptz`, arrays), primary and foreign keys, indexes, `JOIN`, `GROUP BY`, aggregates.
- For Phase 3: window functions (`LAG`, `ROW_NUMBER`), `DISTINCT ON`, date bucketing with
  `date_trunc`.

**Exercise:** start Postgres with `docker compose up -d`, connect with IntelliJ's database tool
(or `docker exec -it galactic-war-postgres psql -U galactic_war`), and by hand create a
`planet` table and a `planet_snapshot` table with a foreign key and an index on
`(planet_index, captured_at)`. Insert a few rows and write a query that returns the latest
snapshot per planet. Drop the tables afterwards; Flyway will create the real ones.

---

## Module 8 — JPA, Hibernate and Flyway

**Why:** this is Phase 2 step 1. JPA maps our Java entities to the tables; Flyway creates and
versions the tables.

**Study:**

- [Spring Guide: Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/).
- [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/reference/): repositories
  and derived query methods (`findTopByPlanetIndexOrderByCapturedAtDesc` style).
- JPA annotations: `@Entity`, `@Table`, `@Id`, `@GeneratedValue(strategy = IDENTITY)`,
  `@Column`, `@ManyToOne` (and why we avoid it for snapshots).
- Why `ddl-auto: validate`: Hibernate checks the entities match the tables but never changes the
  schema. Flyway owns the schema.
- [Flyway docs: Getting started](https://documentation.red-gate.com/flyway): versioned
  migrations, `V1__name.sql` naming, why applied migrations are never edited.
- Pitfalls: the N+1 query problem, lazy loading, why `@Data` on entities is a bad idea.

**Exercise:** in a scratch Spring Boot project (from [start.spring.io](https://start.spring.io/)
with Web, JPA, Flyway, PostgreSQL), create one `V1__create_note.sql` migration and a matching
`Note` entity + `NoteRepository`, then save and read a note from a small controller.

---

## Module 9 — Scheduling and error handling

**Why:** Phase 2's ingestion runs on a timer, not from an HTTP request, so it needs its own
error handling.

**Study:**

- [Spring Guide: Scheduling Tasks](https://spring.io/guides/gs/scheduling-tasks/).
- `fixedDelay` vs `fixedRate` vs `cron`, and why we use `fixedDelay`.
- `@RestControllerAdvice` and `ProblemDetail`: read our `GlobalExceptionHandler`.
- Why the handler does not catch errors from a scheduled job.
- `@Transactional`: what a transaction is, where to put the annotation (service layer).

**Exercise:** in your scratch project, add a job that logs the current time every 10 seconds,
with the interval read from YAML. Make it throw an exception every third run and see what
happens to the next runs with and without a try/catch.

---

## Module 10 — Testing

**Why:** every Phase 2 step should come with tests, and Phase 7 is all about testing.

**Where:** `src/test/java/`. Tests run under the `test` profile (H2 in memory).

**Study:**

- [JUnit user guide](https://junit.org/) (Jupiter API): `@Test`, assertions,
  `@ParameterizedTest`.
- [Mockito](https://site.mockito.org/): `mock`, `when(...).thenReturn(...)`, `verify`.
- [AssertJ](https://assertj.github.io/doc/): fluent assertions (`assertThat(x).isEqualTo(y)`).
- Spring test slices: `@WebMvcTest`, `@DataJpaTest`, `@SpringBootTest`, and when each is worth
  its startup cost.
- Later (Phase 7): [Testcontainers](https://testcontainers.com/) and
  [WireMock](https://wiremock.org/).

**Exercise:** write a unit test for `HelldiversApiService.getCurrentWarId()` with a mocked
`GenericRestClient`: one test for a normal body, one for an empty body that expects
`HelldiversApiEmptyResponseException`.

---

## Module 11 — Docker

**Why:** Postgres runs in Docker now; Grafana and Prometheus join in Phase 6; the app itself
gets a Docker image in Phase 9.

**Study:**

- [Docker: Get started](https://docs.docker.com/get-started/): images vs containers, ports,
  volumes.
- [Docker Compose overview](https://docs.docker.com/compose/): read our
  [docker-compose.yml](../docker-compose.yml) line by line.
- Commands: `docker ps`, `docker logs`, `docker exec`, `docker compose up/down`.

**Exercise:** add a second service to a scratch compose file (for example `adminer` on port
8081), connect it to the Postgres container, and browse the database from the web UI.

---

## Module 12 — Later phases (study when we get there)

| Phase | Topics | Starting points |
|-------|--------|-----------------|
| 3 Analytics | SQL window functions, time-series thinking, service design | Module 7 extras |
| 4 REST API | API design, pagination, DTO vs entity, OpenAPI | [springdoc-openapi](https://springdoc.org/) |
| 5 Security | Authentication vs authorization, OAuth2, JWT | [Spring Security reference](https://docs.spring.io/spring-security/reference/) |
| 6 Observability | Metrics, Actuator, Prometheus, Grafana | [Spring Boot Actuator docs](https://docs.spring.io/spring-boot/reference/actuator/index.html) |
| 7 Testing | Testcontainers, WireMock, integration tests | Module 10 links |
| 8 CI/CD | GitHub Actions workflows | [GitHub Actions docs](https://docs.github.com/en/actions) |
| 9 Deployment | Dockerfile, environment config, hosting | [Spring Boot: Container images](https://docs.spring.io/spring-boot/reference/packaging/container-images/index.html) |

---

## Tips for coming back to code after years

- **Read code before writing it.** Trace one request with the debugger (see the onboarding
  guide, section 5). It teaches more than any tutorial.
- **Small steps.** Make one thing compile and run, commit, then the next.
- **Read the error message to the end.** Spring stack traces are long; the useful line is often
  the last `Caused by:`.
- **Ask in PR reviews.** "Why did you do it this way?" is a good review comment.
- **Use an AI assistant as a tutor.** Ask it to explain a concept or review your code, and ask
  for hints before full answers.
- **Keep a learning log.** A few lines per session: what you learned, what confused you. It
  shows progress over the weeks.
