<div align="center">

# Customer Management System

**A lightweight, full-stack business management platform built with Spring Boot & PrimeFaces.**

[![Java](https://img.shields.io/badge/Java-1.8-orange?logo=openjdk)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-1.5.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![PrimeFaces](https://img.shields.io/badge/PrimeFaces-JSF-blue)](https://www.primefaces.org/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red?logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.md)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL%20%7C%20MySQL-336791?logo=postgresql)](https://www.postgresql.org/)

</div>

---

## What is CMS?

Customer Management System (CMS) is a self-hosted, multi-entity business management tool designed for small to medium organizations. It ships a ready-to-use web UI **and** a JSON REST API to manage **clients, contacts, and custom analytics dashboards** — all from a single deployable JAR.

**Key capabilities:**

- 📋 **Entity management** — full CRUD for clients, persons, industries, types, and countries
- 📊 **Custom dashboards** — define SQL-backed bar, horizontal-bar, and pie charts through the admin UI; no code changes required
- 🔒 **Built-in authentication** — Spring Security login with BCrypt-hashed passwords
- 🔗 **Dual interface** — every feature is accessible via both the PrimeFaces web UI and the REST API simultaneously
- 📤 **Data export** — one-click XLS, CSV, and XML export on every list view

---

## Table of Contents

1. [Architecture](#architecture)
2. [Getting Started](#getting-started)
3. [REST API Examples](#rest-api-examples)
4. [Built With](#built-with)
5. [Contributing](#contributing)
6. [Versioning](#versioning)
7. [Authors](#authors)
8. [License](#license)
9. [Donation](#donation)

---

## Architecture

The application exposes **two parallel interaction layers** backed by the same Spring beans:

```
┌──────────────────────────────────────────────────────────────┐
│                         Client Layer                         │
│   Browser (JSF / PrimeFaces UI)  │  REST client / cURL       │
└───────────────┬──────────────────┴──────────────┬────────────┘
                │  Facelets / EL                  │  JSON HTTP
┌───────────────▼─────────────────────────────────▼────────────┐
│            Controller Layer  (@RestController + @ManagedBean) │
│   PersonController · UnitController · DashboardController    │
│              AbstractController<T>  (CRUD base)              │
└────────────────────────────┬─────────────────────────────────┘
                             │
┌────────────────────────────▼─────────────────────────────────┐
│            Service Layer  (AbstractService<T>)               │
│      JPQL findByName  ·  CrudRepository delegation           │
└────────────────────────────┬─────────────────────────────────┘
                             │
┌────────────────────────────▼─────────────────────────────────┐
│          Repository Layer  (Spring Data CrudRepository)      │
└────────────────────────────┬─────────────────────────────────┘
                             │
                    PostgreSQL  /  MySQL
```

> **Key design constraint:** `AbstractController` derives entity class names at runtime via `ParameterizedType` reflection. The Java entity class name **must** match the `.xhtml` file prefix — e.g. `Person` → `personList.xhtml`, `personView.xhtml`. Violating this breaks both JSF navigation and JPQL queries.

---

## Getting Started

### Prerequisites

| Requirement | Version |
|---|---|
| Java (JDK) | 1.8 |
| Maven | 3.x |
| PostgreSQL **or** MySQL | 9.6+ / 5.7+ |

### 1 · Clone the repository

```bash
git clone https://github.com/your-org/customer-management-system.git
cd customer-management-system
```

### 2 · Set up the database

```sql
CREATE DATABASE cms;
```

Seed reference data by running all `.sql` files in `src/main/resources/`:

```bash
# PostgreSQL example
psql -U postgres -d cms -f src/main/resources/chart_type.sql
psql -U postgres -d cms -f src/main/resources/country.sql
psql -U postgres -d cms -f src/main/resources/data.sql
psql -U postgres -d cms -f src/main/resources/first_name.sql
psql -U postgres -d cms -f src/main/resources/last_name.sql
psql -U postgres -d cms -f src/main/resources/unit_industry.sql
psql -U postgres -d cms -f src/main/resources/unit_type.sql
```

> **MySQL 8.0.4+ only:**
> ```sql
> ALTER USER '${USER}'@'localhost' IDENTIFIED WITH mysql_native_password BY '${PASSWORD}';
> ```
> To switch to MySQL, edit `src/main/resources/application.properties` — comment out the PostgreSQL block and uncomment the MySQL block.

### 3 · Build & run

```bash
mvn clean compile package
java -jar target/customer-management-system-0.0.2-SNAPSHOT.jar
```

The application starts at **[http://localhost:8081/cms](http://localhost:8081/cms)**

| Credential | Value |
|---|---|
| Username | `admin` |
| Password | `123` |

---

## REST API Examples

All entity endpoints follow the same convention: `/{entityName}` maps to full CRUD. The surface is identical for every entity (`/unit`, `/dashboard`, `/country`, etc.).

### Create a person — `POST /person`

**Request**
```http
POST http://localhost:8081/cms/person
Content-Type: application/json

{
  "name": "John Doe",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1-555-0100",
  "notes": "Key contact for Acme Corp"
}
```

**Response** `200 OK`
```json
{
  "id": 42,
  "name": "John Doe",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "+1-555-0100",
  "notes": "Key contact for Acme Corp",
  "createdDate": "2026-03-29T10:00:00.000+0000",
  "endDate": null
}
```

### List all persons — `GET /person`

**Request**
```http
GET http://localhost:8081/cms/person
```

**Response** `200 OK`
```json
[
  {
    "id": 42,
    "name": "John Doe",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "+1-555-0100",
    "createdDate": "2026-03-29T10:00:00.000+0000",
    "endDate": null
  }
]
```

### Register a new user — `POST /api/user/register`

```http
POST http://localhost:8081/cms/api/user/register
Content-Type: application/json

{
  "username": "alice",
  "password": "s3cur3p@ss"
}
```

> The password is automatically BCrypt-hashed before storage. To generate a hash manually, run `SecurityConfig.main()` in `src/main/java/com/cms/configs/SecurityConfig.java`.

### Common REST verbs

| Method | Path | Description |
|---|---|---|
| `GET` | `/{entity}` | List all records |
| `GET` | `/{entity}/{id}` | Fetch a single record |
| `POST` | `/{entity}` | Create a new record |
| `PUT` | `/{entity}` | Update an existing record |
| `DELETE` | `/{entity}/{id}` | Delete a record |

---

## Built With

| Technology | Purpose |
|---|---|
| [Spring Boot 1.5.x](https://spring.io/projects/spring-boot) | Application framework & embedded Tomcat |
| [JoinFaces / PrimeFaces](https://www.primefaces.org/) | JSF component library & UI widgets |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa) | Repository abstraction over JPA / Hibernate |
| [Spring Security](https://spring.io/projects/spring-security) | Authentication & authorization |
| [PostgreSQL](https://www.postgresql.org/) / [MySQL](https://www.mysql.com/) | Relational database (switchable via config) |
| [Apache POI](https://poi.apache.org/) | XLS / XLSX data export |
| [iText](https://itextpdf.com/) | PDF generation |
| [Lombok](https://projectlombok.org/) | Boilerplate reduction (security models & DTOs) |
| [Maven](https://maven.apache.org/) | Build & dependency management |

---

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on the code of conduct and the process for submitting pull requests.

---

## Versioning

This project uses [SemVer](http://semver.org/) for versioning. For available releases, see the [tags on this repository](../../tags).

---

## Authors

- **Sergiu Drahnea** — *Initial work* — [LinkedIn](https://www.linkedin.com/in/sergiu-drahnea/)

---

## License

This project is licensed under the MIT License — see the [LICENSE.md](LICENSE.md) file for details.

---

## Donation

If this project saved you time, consider a ☕ donation via [PayPal](https://www.paypal.me/sdrahnea). Any contribution is warmly appreciated!
