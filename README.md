<div align="center">

# 🍕 Pizzeria POS — Backend API

A RESTful backend for pizzeria management, built with Java 21 and Spring Boot.
Handles the full order lifecycle with role-based access control, automated invoice generation, and sales analytics.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring_Security-session--based-6DB33F?logo=springsecurity)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?logo=mysql)
![Flyway](https://img.shields.io/badge/Flyway-migrations-CC0200?logo=flyway)

</div>

---

## Overview

This is an academic backend project for a pizzeria point-of-sale system. It models the real workflow of a pizzeria with three distinct roles — owner, manager, and kitchen staff — each with their own set of allowed operations.

**What it does:**
- Menu management (CRUD with soft delete, so historical orders are never broken)
- Order creation and lifecycle tracking: `PENDING → READY → INVOICED`
- Automatic invoice generation when an order is marked as delivered
- Business analytics: top-selling pizzas, revenue, and order stats by date range

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Security | Spring Security (session-based auth, BCrypt) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL 8.0+ |
| Migrations | Flyway |
| Utilities | Lombok, Bean Validation |
| Build | Maven |

---

## Architecture

The project follows a standard **3-layer REST architecture**:

```
HTTP Request
    │
    ▼
┌─────────────────────────────┐
│         Controllers         │  ← Thin layer: routing & HTTP status only
│  (AuthController, OrderCtrl,│
│   PizzaCtrl, InvoiceCtrl,   │
│   ReportController)         │
└────────────┬────────────────┘
             │
             ▼
┌─────────────────────────────┐
│          Services           │  ← Business logic, @PreAuthorize, state machine
│ (OrderService, PizzaService,│
│   InvoiceService)           │
└────────────┬────────────────┘
             │
             ▼
┌─────────────────────────────┐
│        Repositories         │  ← JPA interfaces + custom JPQL queries
│  (IOrderRepository,         │
│   IPizzaRepository, ...)    │
└────────────┬────────────────┘
             │
             ▼
          MySQL DB
```

**DTOs** (Java Records) handle all request/response shapes, keeping entities out of the API contract.

**Custom exceptions** (`ResourceNotFoundException`, `BusinessRuleException`) decouple domain errors from HTTP concerns.

---

## Security Model

Authentication is **session-based**: on login, Spring Security creates an HTTP session and stores the `SecurityContext` in it. Subsequent requests are authenticated via the session cookie.

**Password hashing:** BCryptPasswordEncoder — passwords are never stored in plain text.

**Authorization is enforced at the service layer** using `@PreAuthorize` (enabled via `@EnableMethodSecurity`), not the controller layer. This means business rules and access control live together.

### Role Permissions

| Endpoint / Operation | OWNER | MANAGER | COOKER |
|---|:---:|:---:|:---:|
| View menu (active pizzas) | ✅ | ✅ | ✅ |
| Create / Edit / Delete pizza | ✅ | ❌ | ❌ |
| Create / Edit / Delete order | ❌ | ✅ | ❌ |
| Mark order as READY | ❌ | ❌ | ✅ |
| Mark order as INVOICED | ❌ | ✅ | ❌ |
| View invoices | ✅ | ✅ | ❌ |
| Business reports | ✅ | ❌ | ❌ |

---

## Order Lifecycle

Orders flow through three states. Each transition is guarded by both role validation and business rule checks:

```
  [COOKER marks as ready]       [MANAGER marks as invoiced]
         │                               │
         ▼                               ▼
   ┌─────────┐    PATCH /listo/{id}  ┌──────┐    PATCH /facturado/{id}    ┌──────────┐
   │ PENDING │ ────────────────────▶│ READY │ ──────────────────────────▶│ INVOICED │
   └─────────┘                       └──────┘                             └──────────┘
                                          │
                                          └──▶ Invoice auto-generated
                                               (idempotent: one invoice per order)
```

**Business rules enforced in `OrderService`:**
- An order in `READY` state cannot be re-marked as ready (must be `PENDING`)
- An order cannot be invoiced unless it is `READY`
- An invoice cannot be created twice for the same order (`InvoiceService` checks for existing invoice before creating)
- An inactive pizza cannot be added to a new order

---

## API Reference

> **Note:** This is an academic project — endpoint paths follow the Spanish naming convention used during development (`/traer`, `/guardar`, etc.).

### Auth

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/auth/login` | Public | Authenticate and start a session |
| `POST` | `/auth/logout` | Public | Invalidate the current session |

**Login request body:**
```json
{
  "username": "Mostrador",
  "password": "yourpassword"
}
```

**Login response:**
```json
{
  "username": "Mostrador",
  "role": "MANAGER",
  "message": "Login exitoso"
}
```

---

### Pizza (Menu)

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/pizza/traer` | Public | List all active pizzas |
| `POST` | `/pizza/guardar` | OWNER | Create a new pizza |
| `PUT` | `/pizza/editar/{id}` | OWNER | Update a pizza |
| `DELETE` | `/pizza/eliminar/{id}` | OWNER | Soft delete a pizza |

> `DELETE` performs a **soft delete** — sets `active = false` rather than removing the row. This preserves price history in past orders.

**Pizza fields:** `name`, `description`, `price`, `size` (`SMALL`/`MEDIUM`/`LARGE`), `cookingType` (`PIEDRA`/`PARRILLA`/`MOLDE`)

---

### Orders

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/pedido/traer` | Public | List all orders |
| `POST` | `/pedido/guardar` | MANAGER | Create a new order |
| `PUT` | `/pedido/editar/{id}` | MANAGER | Update an order |
| `DELETE` | `/pedido/eliminar/{id}` | MANAGER | Delete an order |
| `PATCH` | `/pedido/editar/listo/{id}` | COOKER | Transition: `PENDING → READY` |
| `PATCH` | `/pedido/editar/facturado/{id}` | MANAGER | Transition: `READY → INVOICED` + auto invoice |

**Create order request body:**
```json
{
  "clientName": "Table 5",
  "timeEstimated": 20,
  "items": [
    { "pizzaId": 1, "quantity": 2 },
    { "pizzaId": 3, "quantity": 1 }
  ]
}
```

> Unit prices are **snapshotted at order time** from the pizza's current price, so future price changes don't affect existing orders.

---

### Invoices

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/factura/traer` | OWNER, MANAGER | List all invoices |

---

### Reports (OWNER only)

All report endpoints accept `start` and `end` query params as `LocalDateTime` (`yyyy-MM-ddTHH:mm:ss`).

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/reportes/pizzas-mas-pedidas` | Most ordered pizzas in a date range, sorted by quantity |
| `GET` | `/reportes/ingresos` | Total revenue for a date range |
| `GET` | `/reportes/pedidos-por-periodo` | Order count + total revenue for a date range |

**Example:** `GET /reportes/ingresos?start=2025-01-01T00:00:00&end=2025-12-31T23:59:59`

> Reports are powered by **custom JPQL queries** using constructor expressions (`new StatsDto(...)`) for type-safe aggregation without loading full entities.

---

## Data Model

```
┌──────────┐        ┌────────────┐        ┌───────────┐
│  usuario │        │   pedido   │        │  factura  │
│──────────│        │────────────│        │───────────│
│ id       │        │ numero     │◀───────│ numero    │
│ nombre   │        │ nombre_cli │  1:1   │ emision   │
│ contrasena        │ fecha_hora │        │ order_id  │
│ rol      │        │ estado     │        └───────────┘
│ enabled  │        │ tiempo_est │
└──────────┘        └─────┬──────┘
                          │ 1:N
                          ▼
                   ┌─────────────┐        ┌──────────────┐
                   │pizza_pedido │        │    pizza     │
                   │─────────────│        │──────────────│
                   │ id          │───────▶│ id          │
                   │ cantidad    │  N:1   │ nombre       │
                   │ precio_unit │        │ descripcion  │
                   │ pizza_id    │        │ precio       │
                   │ order_id    │        │ tamano       │
                   └─────────────┘        │ tipo_coccion │
                                          │ active       │
                                          └──────────────┘
```

---

## Setup

### Prerequisites

- Java 21
- MySQL 8.0+ (the Flyway migration uses `RENAME COLUMN`, which requires MySQL 8.0)
- Maven

### 1. Clone and configure

```bash
git clone https://github.com/facuzambellini5/pizzeria.git
cd pizzeria
```

Create `src/main/resources/application.properties` (this file is gitignored):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pizzeria_db?createDatabaseIfNotExist=true
spring.datasource.username=your_mysql_user
spring.datasource.password=your_mysql_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

### 2. Seed initial users

The `DataInitializer.java` class is commented out. You can re-enable it temporarily to seed users on first startup, or insert them manually:

```sql
-- Passwords are BCrypt-hashed. These hashes correspond to "1234"
INSERT INTO usuario (nombre_usuario, contrasena, rol, enabled) VALUES
  ('Mostrador', '$2a$10$...', 'MANAGER', true),
  ('Dueno',     '$2a$10$...', 'OWNER',   true),
  ('Cocina',    '$2a$10$...', 'COOKER',  true);
```

> To generate a BCrypt hash for a password, you can use: https://bcrypt-generator.com

### 3. Run

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.

Flyway will run `V1__rename_tables_and_columnas.sql` automatically on startup to align the schema with the entity mappings.

---

## Key Design Decisions

**Why session-based auth instead of JWT?**
This is a pizzeria POS — a controlled, internal environment with a single frontend client on the same network. Stateful sessions are simpler to implement correctly and appropriate for this context. For a public API with distributed clients, JWT would be the better choice.

**Why is `@PreAuthorize` on the service layer, not the controller?**
Putting access control in services means the rules travel with the business logic. If a method is ever called from another service (internal call), it's still protected. Controllers that call a protected service method will be blocked regardless of how the call arrived.

**Why soft delete for pizzas?**
Deleting a pizza from the DB would orphan or corrupt historical `pizza_pedido` records that reference it. With soft delete (`active = false`), past orders retain their full data while the item disappears from the active menu. `findByActiveTrue()` handles the filtering automatically.

**Why snapshot `unitPrice` in `PizzaOrder`?**
If a pizza's price changes after an order is placed, the invoice would show the wrong amount. Storing `unitPrice` at order creation time freezes the price for each line item, matching real-world invoicing behavior.

**Why `StatsDto` with a JPQL constructor expression?**
Reports like `getOrderStats()` need aggregated data, not full entity objects. Using `new StatsDto(SUM(...), COUNT(...))` in JPQL lets the database do the aggregation and returns a strongly-typed record directly — no manual mapping, no loading of unnecessary data.

---

## Author

**Facundo González Zambellini**
[LinkedIn](https://linkedin.com/in/facundo-gonzález-zambellini) · [GitHub](https://github.com/facuzambellini5)
