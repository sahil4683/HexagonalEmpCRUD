# Hexagonal Emp CRUD

A simple Java Spring Boot CRUD application using hexagonal (ports & adapters) architecture.

---

## 📝 Overview
- **Language:** Java 17
- **Framework:** Spring Boot
- **Persistence:** Spring Data JPA, H2 (in-memory)
- **API Docs:** OpenAPI/Swagger UI (springdoc)

---

## ✨ Features
- Clean separation: REST API, domain logic, persistence
- Employee ↔ Address: One-to-many relationship
- Sample data auto-loaded on startup
- OpenAPI UI for easy API testing

---

## 📁 Project Structure

```
io.sp.hexagonal_emp_c_r_u_d
├── HexagonalEmpCRUDApplication.java         # Main entry
├── config/
│   ├── DomainConfig.java
│   └── JacksonConfig.java
├── domain/
│   ├── model/
│   │   ├── EmployeeDto.java
│   │   └── AddressDto.java
│   ├── port/
│   │   └── in/EmployeeUseCase.java
│   └── service/EmployeeService.java
├── infrastructure/
│   ├── adapter/
│   │   ├── in/rest/EmployeeResource.java    # REST controller
│   │   └── out/persistence/
│   │       ├── adapter/EmployeePersistenceAdapter.java
│   │       ├── entity/Employee.java, Address.java
│   │       └── repository/EmployeeRepository.java, AddressRepository.java
│   └── configuration/
│       ├── BeanConfiguration.java
│       └── DataInitializer.java             # Loads sample data
```

---

## 🔄 Data Flow (REST → Domain → Persistence)

```
Client (HTTP Request)
    │
    ▼
EmployeeResource
[Class - REST Controller]
Receives HTTP requests and returns responses
Uses DTOs for communication

    │
    │ calls
    ▼
EmployeeUseCase
[Interface - Input Port]
Defines the operations available for employee management

    │
    │ implemented by
    ▼
EmployeeService
[Class - Domain Service]
Contains the business logic for employee operations

    │
    │ delegates persistence work to
    ▼
EmployeePersistenceAdapter
[Class - Persistence Adapter]
Handles conversion between DTOs and Entities

    │
    │ uses
    ▼
EmployeeRepository
[Interface - Spring Data JPA Repository]
Performs database operations

    │
    │ works with
    ▼
Employee / Address
[Entities - JPA Entities]

    │
    ▼
H2 Database
[In-Memory Database]
```

---

## 🏁 How to Run

1. **Start app:**
   ```
   mvn spring-boot:run
   ```
2. **Open Swagger UI:**
   - [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🗃 Sample Data
- Defined in `DataInitializer.java` (runs at startup if DB is empty)
- Example:
  ```json
  [
    {
      "name": "Sahil",
      "contactNumber": "32532",
      "addresses": [
        { "city": "Mumbai", "country": "IND" },
        { "city": "Pune", "country": "IND" }
      ]
    },
    // ...more employees
  ]
  ```

---

## 🛠 Troubleshooting

### LazyInitializationException
- **Cause:** Accessing a lazy-loaded collection after the JPA session is closed.
- **Solution in this project:**
  - `EmployeeRepository` fetches addresses using `JOIN FETCH`
  - `EmployeePersistenceAdapter` converts entities → DTOs inside the transaction
  - Controllers work only with DTOs

---

## 🎯 Benefits
- Clear separation of concerns
- Domain logic independent from frameworks
- Easier unit testing
- Flexible replacement of external systems
- Clean DTO ↔ Entity mapping

---

## 🚀 Extending This Project
- Add more APIs (update, delete, search, etc.)
- Add validation, error handling, authentication
- Use a persistent DB (e.g., PostgreSQL)
- Add integration tests

---
