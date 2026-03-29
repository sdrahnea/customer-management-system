# AGENTS.md — Customer Management System

## Overview
Spring Boot 1.5.x + JSF (PrimeFaces) monolith using JoinFaces (`jsf-spring-boot-parent 2.4.1`). Runs on port **8081** at context path `/cms`. Dual-database support (MySQL or PostgreSQL) — PostgreSQL is the active default in `application.properties`.

## Architecture

The app has two parallel interaction layers sharing the same controller beans:

1. **JSF / PrimeFaces UI** — `.xhtml` views under `src/main/resources/META-INF/resources/`. Views bind directly to Spring beans via EL (e.g., `#{personController.list}`). Navigation is string-based outcome matching (e.g., returning `"personList"` navigates to `personList.xhtml`).
2. **REST API** — same controller classes also expose `@RestController` endpoints (e.g., `GET /person`, `POST /person`).

## Generic Layered Pattern

Every domain entity follows this strict, uniform pattern. When adding a new entity, replicate this structure:

| Layer | Base Class | Example Concrete Class |
|---|---|---|
| Model | `CoreEntity` (id, name, notes, createdDate, endDate) | `Person.java` |
| Repository | `CrudRepository<T, Long>` | `PersonRepository.java` |
| Service | `AbstractService<T>` | `PersonService.java` |
| Controller | `AbstractController<T>` | `PersonController.java` |
| JSF Converter | `AbstractConverter<T>` + `@FacesConverter("xyzConverter")` | `PersonConverter.java` |
| Views | `xyzList.xhtml` + `xyzView.xhtml` (templated from `main.xhtml`) | `personList.xhtml` |

`AbstractController` uses reflection (`ParameterizedType`) to derive the entity class name at runtime for both JPQL queries and JSF navigation outcomes — **the entity class name must match the xhtml file prefix** (e.g., `Person` → `personList`, `personView`).

## Dashboard / Chart Engine
`Dashboard` entities store raw SQL (`cquery` column) executed directly via `EntityManager` at runtime. `DashboardViewController` iterates dashboards, checks `show` constant, runs the query, and builds PrimeFaces chart models (`BarChartModel`, `PieChartModel`, etc.) driven by `chartType.name`. Series tags are stored as a comma-separated string in `seriesTags` and accessed via `getSeriesList()`.

## Security
- Spring Security configured in `SecurityConfig.java`; login form at `/login.xhtml` with custom username/password parameters (`form:username`, `form:password`).
- Users stored in `users` table as `SecurityUser` (BCrypt passwords). Register via `POST /api/user/register`.
- CSRF is **disabled** (`http.csrf().disable()`).
- To generate a BCrypt hash for a new password run `SecurityConfig.main()` directly.

## Key Files
- `src/main/java/com/cms/controller/AbstractController.java` — dual-mode (JSF + REST) base, CRUD wired to service
- `src/main/java/com/cms/service/AbstractService.java` — generic JPQL `findByName`, delegates to `CrudRepository`
- `src/main/java/com/cms/controller/DashboardViewController.java` — dynamic chart construction from stored SQL
- `src/main/resources/application.properties` — DB credentials, JSF theme, server port
- `src/main/resources/META-INF/resources/main.xhtml` — Facelets template for all list/view pages

## Build & Run
```bash
# Build
mvn clean compile package

# Run
java -jar target/customer-management-system-0.0.2-SNAPSHOT.jar

# App is available at http://localhost:8081/cms
```

## Database Setup
1. Create schema: `CREATE DATABASE cms;`
2. Seed data by executing all `.sql` files in `src/main/resources/` (chart_type, country, data, first_name, last_name, unit_industry, unit_type).
3. For MySQL 8.0.4+: `ALTER USER '${USER}'@'localhost' IDENTIFIED WITH mysql_native_password BY '${PASSWORD}';`
4. To switch to MySQL, comment out the PostgreSQL block and uncomment the MySQL block in `application.properties`.

## Conventions
- Models do **not** use Lombok (manual getters/setters), except `SecurityUser` and some controllers which use `@Data`.
- All `@RestController` controllers also carry `@ManagedBean`/`@RequestScoped` for JSF — both annotations coexist on the same class.
- JSF converters use OmniFaces `SelectItemsUtils.findValueByStringConversion` for object resolution; every entity needing a `<p:selectOneMenu>` needs its own `@FacesConverter`.
- Export icons (XLS/CSV/XML) in list views reference `/images/icon/` under the resources icon folder.

