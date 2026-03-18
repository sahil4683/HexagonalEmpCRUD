# API Reference

Base URL: `http://localhost:8080`

## Employee API

### `GET /api/employees`

Returns all employees with their addresses and department id.

Example response:

```json
[
  {
    "id": 1,
    "name": "Sahil",
    "contactNumber": "32532",
    "addresses": [
      {
        "id": 1,
        "city": "Mumbai",
        "country": "IND"
      },
      {
        "id": 2,
        "city": "Pune",
        "country": "IND"
      }
    ],
    "departmentId": 1
  }
]
```

### `GET /api/employees/{id}`

Returns a single employee.

Error responses:

- `404 Not Found` if the employee does not exist

Example:

```bash
curl http://localhost:8080/api/employees/1
```

### `POST /api/employees`

Creates an employee and returns the generated id.

Error responses:

- `400 Bad Request` if `departmentId` does not reference an existing department

Example request:

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
    }
  ]
}
```

Example command:

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Anita\",\"contactNumber\":\"9988001122\",\"departmentId\":1,\"addresses\":[{\"id\":null,\"city\":\"Pune\",\"country\":\"IND\"}]}"
```

Success response:

```json
6
```

### `PUT /api/employees/{id}`

Updates the employee and returns the id.

Important behavior:

- the address list is cleared and replaced with the submitted list
- setting `departmentId` to `null` removes the employee from a department

Error responses:

- `404 Not Found` if the employee does not exist
- `400 Bad Request` if `departmentId` does not reference an existing department

Example request:

```json
{
  "name": "Anita Sharma",
  "contactNumber": "9988001133",
  "departmentId": 2,
  "addresses": [
    {
      "id": null,
      "city": "Bengaluru",
      "country": "IND"
    }
  ]
}
```

### `DELETE /api/employees/{id}`

Deletes the employee.

Success status: `204 No Content`

Error responses:

- `404 Not Found` if the employee does not exist

## Department API

### `GET /api/departments`

Returns all departments.

Example response:

```json
[
  {
    "id": 1,
    "name": "Engineering"
  },
  {
    "id": 2,
    "name": "HR"
  }
]
```

### `GET /api/departments/{id}`

Returns a single department.

Error responses:

- `404 Not Found` if the department does not exist

### `POST /api/departments`

Creates a department and returns the generated id.

Example request:

```json
{
  "name": "Finance"
}
```

### `PUT /api/departments/{id}`

Updates the department name.

Error responses:

- `404 Not Found` if the department does not exist

Example request:

```json
{
  "name": "People Operations"
}
```

### `DELETE /api/departments/{id}`

Deletes the department.

Success status: `204 No Content`

Error responses:

- `404 Not Found` if the department does not exist
- `409 Conflict` if employees are still assigned to the department

### `GET /api/departments/{id}/employees`

Returns employees whose `departmentId` matches the department id.

Example:

```bash
curl http://localhost:8080/api/departments/1/employees
```

## Validation Rules

### EmployeeDto

- `name` is required
- `name` max length is 255
- `contactNumber` max length is 255

### DepartmentDto

- `name` is required
- `name` max length is 255

### AddressDto

- `city` max length is 255
- `country` max length is 255

## OpenAPI

Swagger UI:

- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Only `/api/**` routes are included in the generated OpenAPI documentation.

## Known Behavior Notes

- The department employee listing is assembled in memory from the full employee list.
- Nested address validation is limited because the employee address collection is not annotated with `@Valid`.
- Department deletes are intentionally blocked when employees are still assigned.
