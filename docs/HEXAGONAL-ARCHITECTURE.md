# Hexagonal Architecture In This Project

This document explains how hexagonal architecture works in `HexagonalEmpCRUD` and how the current code maps to the pattern.

## What Hexagonal Architecture Means

Hexagonal architecture, also called ports and adapters, is a way to structure an application so that:

- business logic stays at the center
- frameworks and external systems stay at the edges
- the core application depends on abstractions, not infrastructure details

In practice, this means the domain should not know whether data comes from:

- a REST API
- a database
- a message queue
- a file
- a test stub

The domain only talks to ports. Adapters implement those ports.

## The Main Idea

You can picture the app like this:

```text
        Inbound Side                          Core                          Outbound Side

  HTTP request / Swagger / tests  ->  Use cases + domain services  ->  Repository ports
                                                                      ->  Persistence adapters
                                                                      ->  Spring Data JPA / H2
```

Or in project terms:

```text
Client
  -> REST controller
  -> use case interface
  -> domain service
  -> repository port
  -> persistence adapter
  -> JPA repository
  -> database
```

## How This Project Is Organized

The codebase is split into three important areas:

### 1. Domain core

Located under:

- `src/main/java/io/sp/hexagonal_emp_c_r_u_d/domain`

This layer contains:

- DTOs used by the application core
- input ports
- output ports
- domain services

The domain defines what the application can do, without depending on Spring MVC controllers or JPA entities.

### 2. Inbound adapters

Located under:

- `src/main/java/io/sp/hexagonal_emp_c_r_u_d/infrastructure/adapter/in/rest`

These classes accept requests from the outside world and translate them into use-case calls.

In this project:

- [EmployeeResource.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/infrastructure/adapter/in/rest/EmployeeResource.java)
- [DepartmentController.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/infrastructure/adapter/in/rest/DepartmentController.java)

### 3. Outbound adapters

Located under:

- `src/main/java/io/sp/hexagonal_emp_c_r_u_d/infrastructure/adapter/out/persistence`

These classes connect the domain to external systems, which here means the database.

In this project:

- persistence adapters implement repository ports
- JPA entities model the database shape
- Spring Data repositories do the actual database operations

## Core Building Blocks

## Input Ports

Input ports define the operations the application exposes.

Examples:

- [EmployeeUseCase.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/domain/port/in/EmployeeUseCase.java)
- [DepartmentUseCase.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/domain/port/in/DepartmentUseCase.java)

These interfaces answer the question:

"What can the application do?"

For example, `EmployeeUseCase` exposes:

- find all employees
- get one employee
- create an employee
- update an employee
- delete an employee

The controller depends on this interface instead of depending directly on a repository.

## Domain Services

Domain services implement the input ports.

Examples:

- [EmployeeService.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/domain/service/EmployeeService.java)
- [DepartmentService.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/domain/service/DepartmentService.java)

These classes contain application behavior such as:

- orchestration
- not-found handling
- delegation to output ports

They do not know about Spring MVC annotations or JPA queries.

## Output Ports

Output ports define what the core needs from external systems.

Examples:

- [EmployeeRepositoryPort.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/domain/port/out/EmployeeRepositoryPort.java)
- [DepartmentRepositoryPort.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/domain/port/out/DepartmentRepositoryPort.java)

These interfaces answer the question:

"What does the domain need from persistence?"

That keeps the service layer independent from JPA and database-specific code.

## Inbound Adapters

Inbound adapters translate external input into use-case calls.

### Employee controller example

[EmployeeResource.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/infrastructure/adapter/in/rest/EmployeeResource.java) does a few very specific jobs:

- maps HTTP routes like `GET /api/employees`
- validates request bodies with `@Valid`
- calls `EmployeeUseCase`
- converts the result into HTTP responses

What it does not do:

- it does not contain SQL
- it does not build JPA queries
- it does not know how entities are stored

That separation is the point of the pattern.

## Outbound Adapters

Outbound adapters implement the output ports.

### Employee persistence example

[EmployeePersistenceAdapter.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/infrastructure/adapter/out/persistence/adapter/EmployeePersistenceAdapter.java) implements `EmployeeRepositoryPort`.

This class is responsible for:

- loading entities from repositories
- mapping entities to DTOs
- mapping DTOs back to entities
- resolving `departmentId` into a managed `Department`
- enforcing persistence-related errors such as invalid foreign-key references

This is a good example of adapter code because it isolates infrastructure details from the domain service.

## Entities And Repositories

The persistence layer uses:

- JPA entities such as `Employee`, `Department`, and `Address`
- Spring Data repositories such as `EmployeeRepository` and `DepartmentRepository`

These classes belong to infrastructure, not to the domain core.

That matters because entity classes are shaped by persistence concerns:

- `@Entity`
- `@Id`
- `@OneToMany`
- `@ManyToOne`

Those are framework details the core should not depend on.

## Wiring The Application

Hexagonal architecture still needs composition somewhere. In this project that happens in Spring configuration.

### Bean wiring

[BeanConfiguration.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/infrastructure/configuration/BeanConfiguration.java) connects interfaces to implementations:

- `EmployeeUseCase` -> `EmployeeService`
- `DepartmentUseCase` -> `DepartmentService`

This is where Spring assembles the application.

### Persistence scanning

[DomainConfig.java](/C:/Users/sahil/Downloads/HexagonalEmpCRUD/HexagonalEmpCRUD/src/main/java/io/sp/hexagonal_emp_c_r_u_d/config/DomainConfig.java) enables:

- entity scanning
- JPA repository scanning
- transaction management

This keeps the infrastructure bootstrapping outside the domain logic.

## End-To-End Example

Here is the employee read flow:

```text
GET /api/employees/1
  -> EmployeeResource.getEmployee(...)
  -> EmployeeUseCase.get(...)
  -> EmployeeService.get(...)
  -> EmployeeRepositoryPort.findById(...)
  -> EmployeePersistenceAdapter.findById(...)
  -> EmployeeRepository.findById(...)
  -> database
  -> entity mapped to EmployeeDto
  -> JSON response
```

Here is the employee create flow:

```text
POST /api/employees
  -> EmployeeResource.createEmployee(...)
  -> EmployeeUseCase.create(...)
  -> EmployeeService.create(...)
  -> EmployeeRepositoryPort.create(...)
  -> EmployeePersistenceAdapter.create(...)
  -> DepartmentRepository.findById(...) if departmentId is provided
  -> EmployeeRepository.save(...)
  -> generated id returned to client
```

## Why This Structure Helps

Benefits in this project:

- controllers stay thin
- domain services are easier to test
- persistence details are isolated
- swapping the REST layer or persistence layer becomes easier
- the application flow is easier to reason about

For example, if later you want to expose the same employee use cases through:

- GraphQL
- a CLI
- messaging

you can add new inbound adapters without rewriting the core service contracts.

Likewise, if you move from H2/JPA to another persistence mechanism, you can replace the outbound adapter while keeping the use cases stable.

## What Is Not Fully Hexagonal Yet

This project is already structured around ports and adapters, but a few choices still mix responsibilities slightly:

- `ResponseStatusException` is thrown from services and persistence adapters, which couples the core more tightly to web semantics than ideal
- the domain model uses DTOs as the application model, instead of having separate pure domain objects and API contracts
- `GET /api/departments/{id}/employees` currently filters in memory instead of using a dedicated query port

These are common tradeoffs in small Spring projects, and the structure is still much cleaner than a controller-repository direct coupling.

## How To Extend The Project The Hexagonal Way

If you add a new feature, a good workflow is:

1. Define or extend the input port.
2. Implement the behavior in the domain service.
3. Define or extend the output port if the core needs infrastructure support.
4. Implement that output port in an adapter.
5. Expose the feature through an inbound adapter such as REST.

Example:

If you want "search employees by department and city":

1. add a method to `EmployeeUseCase`
2. implement it in `EmployeeService`
3. add the needed method to `EmployeeRepositoryPort`
4. implement it in `EmployeePersistenceAdapter`
5. add a REST endpoint in `EmployeeResource`

## Quick Reference

### Core

- `domain/model`
- `domain/port/in`
- `domain/port/out`
- `domain/service`

### Inbound adapters

- `infrastructure/adapter/in/rest`

### Outbound adapters

- `infrastructure/adapter/out/persistence/adapter`
- `infrastructure/adapter/out/persistence/repository`
- `infrastructure/adapter/out/persistence/entity`

### Configuration

- `config`
- `infrastructure/configuration`

## Summary

In this project, hexagonal architecture means:

- controllers talk to use cases
- use cases are implemented by services
- services talk to repository ports
- adapters implement those ports
- repositories and JPA stay at the edge

That keeps the application core more stable, more testable, and easier to evolve.
