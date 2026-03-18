# HexagonalEmpCRUD

Spring Boot CRUD application for managing employees, departments, and employee addresses using a hexagonal architecture.

## Overview

This project demonstrates how to organize a small REST API around:

- domain use cases and services
- input ports and output ports
- REST controllers as inbound adapters
- JPA persistence as an outbound adapter

The app starts with an in-memory H2 database and seeds a small dataset automatically when no employees exist yet.

## Tech Stack

- Java 17
- Spring Boot 4.0.3
- Spring Web
- Spring Data JPA
- H2 database
- Jakarta Validation
- Springdoc OpenAPI / Swagger UI
- Lombok
- `error-handling-spring-boot-starter`

## Architecture

The codebase follows a ports-and-adapters structure:

```text
src/main/java/io/sp/hexagonal_emp_c_r_u_d
|-- config
|   |-- DomainConfig.java
|   `-- JacksonConfig.java
|-- domain
|   |-- model
|   |   |-- AddressDto.java
|   |   |-- DepartmentDto.java
|   |   `-- EmployeeDto.java
|   |-- port
|   |   |-- in
|   |   |   |-- DepartmentUseCase.java
|   |   |   `-- EmployeeUseCase.java
|   |   `-- out
|   |       |-- DepartmentRepositoryPort.java
|   |       `-- EmployeeRepositoryPort.java
|   `-- service
|       |-- DepartmentService.java
|       `-- EmployeeService.java
`-- infrastructure
    |-- adapter
    |   |-- in/rest
    |   |   |-- DepartmentController.java
    |   |   `-- EmployeeResource.java
    |   `-- out/persistence
    |       |-- adapter
    |       |   |-- DepartmentPersistenceAdapter.java
    |       |   `-- EmployeePersistenceAdapter.java
    |       |-- entity
    |       |   |-- Address.java
    |       |   |-- Department.java
    |       |   `-- Employee.java
    |       `-- repository
    |           |-- AddressRepository.java
    |           |-- DepartmentRepository.java
    |           `-- EmployeeRepository.java
    `-- configuration
        |-- BeanConfiguration.java
        `-- DataInitializer.java
```

### Request Flow

```text
HTTP Request
  -> REST Controller
  -> Use Case (input port)
  -> Domain Service
  -> Repository Port (output port)
  -> Persistence Adapter
  -> Spring Data JPA Repository
  -> H2 Database
```

## Domain Model

### Employee

- `id: Long`
- `name: String` required, max 255 chars
- `contactNumber: String` optional, max 255 chars
- `departmentId: Long` optional
- `addresses: List<AddressDto>`

### Address

- `id: Long`
- `city: String` optional, max 255 chars
- `country: String` optional, max 255 chars

### Department

- `id: Long`
- `name: String` required, max 255 chars

## Available APIs

### Employee endpoints

- `GET /api/employees`
- `GET /api/employees/{id}`
- `POST /api/employees`
- `PUT /api/employees/{id}`
- `DELETE /api/employees/{id}`

### Department endpoints

- `GET /api/departments`
- `GET /api/departments/{id}`
- `POST /api/departments`
- `PUT /api/departments/{id}`
- `DELETE /api/departments/{id}`
- `GET /api/departments/{id}/employees`

Detailed examples are in [docs/API.md](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/docs/API.md).

## Running the Application

### Prerequisites

- Java 17+
- Maven 3.9+ or the included Maven wrapper

### Start locally

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The app starts on `http://localhost:8080`.

## Swagger UI

Open:

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)

Springdoc is configured to expose only endpoints under `/api/**`.

## Database Configuration

The default datasource is H2 in-memory:

```properties
spring.datasource.url=jdbc:h2:mem:HexagonalEmpCRUD
spring.datasource.username=sa
spring.datasource.password=
```

You can override the datasource through environment variables:

- `JDBC_DATABASE_URL`
- `JDBC_DATABASE_USERNAME`
- `JDBC_DATABASE_PASSWORD`

Other relevant persistence settings:

- `spring.jpa.hibernate.ddl-auto=update`
- `spring.jpa.open-in-view=false`
- `spring.jpa.show-sql=true`

## Seed Data

At startup, `DataInitializer` inserts sample departments and employees when the employee table is empty.

### Seed departments

- Engineering
- HR

### Seed employees

- Sahil -> Engineering
- Rohan -> Engineering
- Amit -> HR
- Neha -> HR
- Priya -> no department

Each employee is also seeded with two addresses.

## Example Payloads

### Create a department

```json
{
  "name": "Finance"
}
```

### Create an employee

```json
{
  "name": "Anita",
  "contactNumber": "9988001122",
  "departmentId": 1,
  "addresses": [
    {
      "id": null,
      "city": "Pune",
      "country": "IND"
    },
    {
      "id": null,
      "city": "Nashik",
      "country": "IND"
    }
  ]
}
```

## Validation and Error Handling

- `EmployeeDto.name` is required
- `DepartmentDto.name` is required
- max length for textual fields is 255 characters
- request validation is enabled through `@Valid`
- JSON error responses are enabled through `error-handling-spring-boot-starter`
- missing employees and departments now return `404 Not Found`
- invalid `departmentId` in employee create/update returns `400 Bad Request`
- deleting a department with assigned employees returns `409 Conflict`

## Important Implementation Notes

- Employee reads use `JOIN FETCH` to load addresses and departments eagerly for API responses.
- `spring.jpa.open-in-view=false` is enabled, so entity-to-DTO mapping happens before the web layer accesses the data.
- Updating an employee replaces the current address collection with the submitted list.
- Employee create/update resolves `departmentId` against the database and rejects invalid references.
- Deleting a department is blocked when employees are still assigned to it.
- `GET /api/departments/{id}/employees` currently filters employees in memory using `EmployeeUseCase.findAll()`.
- Department entities no longer eagerly load their employee collection for normal department reads.

## Development Notes

Areas that would be good next improvements:

- add unit and integration tests
- add explicit API error examples
- move department employee lookup to a repository query
- tighten DTO validation for nested addresses
- consider persistent storage such as PostgreSQL

## Documentation Files

- [README.md](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/README.md): project overview and setup
- [docs/API.md](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/docs/API.md): endpoint reference and example requests
